package Part3_Solutions_to_Execises.P10_Data_Validation

import java.util.function.Predicate

object P4_Transforming_Data {

  import cats.Semigroup
  import cats.data.Validated
  import cats.syntax.semigroup._ // for |+|
  import cats.syntax.apply._ // for mapN
  import cats.syntax.validated._
  import cats.data.Validated._ // for Valid and Invalid
  sealed trait Predicate[E, A] {

    import Predicate._

    def and(that: Predicate[E, A]): Predicate[E, A] =
      And(this, that)

    def or(that: Predicate[E, A]): Predicate[E, A] =
      Or(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
      this match {
        case Pure(func) =>
          func(a)
        case And(left, right) =>
          (left(a), right(a)).mapN((_, _) => a)
        case Or(left, right) =>
          left(a) match {
            case Valid(a1) => a1.valid
            case Invalid(e1) =>
              right(a) match {
                case Valid(a2) => a2.valid
                case Invalid(e2) => (e1 |+| e2).invalid
              }
          }
      }
  }

  object Predicate {

    def apply[E, A](f: A => Validated[E, A]): Predicate[E, A] = Pure(f)

    def lift[E, A](err: E, fn: A => Boolean): Predicate[E, A] =
      Pure(a => if (fn(a)) a.valid else err.invalid)

    final case class And[E, A](
                                left: Predicate[E, A],
                                right: Predicate[E, A]) extends Predicate[E, A]

    final case class Or[E, A](
                               left: Predicate[E, A],
                               right: Predicate[E, A]) extends Predicate[E, A]

    final case class Pure[E, A](
                                 func: A => Validated[E, A]) extends Predicate[E, A]

  }


  import cats.Semigroup
  import cats.data.Validated

  sealed trait Check[E, A, B] {

    import Check._

    def apply(in: A)(implicit s: Semigroup[E]): Validated[E, B]

    def map[C](f: B => C): Check[E, A, C] =
      Map(this, f)

    def flatMap[C](f: B => Check[E, A, C]): FlatMap[E, A, B, C] =
      FlatMap(this, f)

    def andThen[C](next: Check[E, B, C]): Check[E, A, C] =
      AndThen(this, next)
  }

  final case class FlatMap[E, A, B, C](check: Check[E, A, B],
                                       func: B => Check[E, A, C]) extends Check[E, A, C] {
    override def apply(in: A)(implicit s: Semigroup[E]): Validated[E, C] =
      check(in).withEither(_.flatMap(b => func(b)(in).toEither))
  }

  object Check {

    def apply[E, A](pred: Predicate[E, A]): Check[E, A, A] = PurePredicate(pred)

    def apply[E, A, B](func: A => Validated[E, B]): Check[E, A, B] = Pure(func)

    final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C) extends Check[E, A, C] {
      override def apply(in: A)(implicit s: Semigroup[E]): Validated[E, C] =
        check(in).map(func)
    }

    final case class PurePredicate[E, A](pred: Predicate[E, A]) extends Check[E, A, A] {
      override def apply(in: A)(implicit s: Semigroup[E]): Validated[E, A] = pred(in)
    }

    final case class Pure[E, A, B](func: A => Validated[E, B]) extends Check[E, A, B] {
      override def apply(in: A)(implicit s: Semigroup[E]): Validated[E, B] =
        func(in)
    }

    final case class AndThen[E, A, B, C](check1: Check[E, A, B], check2: Check[E, B, C])
      extends Check[E, A, C] {
      override def apply(in: A)(implicit s: Semigroup[E]): Validated[E, C] =
        check1(in).withEither(_.flatMap(b => check2(b).toEither))
    }

  }


  def main(args: Array[String]): Unit = {

    import cats.data.NonEmptyList
    type Errors = NonEmptyList[String]

    def error(s: String): NonEmptyList[String] =
      NonEmptyList(s, Nil)

    def longerThan(n: Int): Predicate[Errors, String] =
      Predicate.lift(
        error(s"Must be longer than $n characters"),
        str => str.length > n)

    val alphanumeric: Predicate[Errors, String] =
      Predicate.lift(
        error(s"Must be all alphanumeric characters"),
        str => str.forall(_.isLetterOrDigit))

    def contains(char: Char): Predicate[Errors, String] =
      Predicate.lift(
        error(s"Must contain the character $char"),
        str => str.contains(char))

    def containsOnce(char: Char): Predicate[Errors, String] =
      Predicate.lift(
        error(s"Must contain the character $char only once"),
        str => str.count(c => c == char) == 1)


    def checkUsername: Check[Errors, String, String] =
      Check(longerThan(3) and alphanumeric)

    val splitEmail: Check[Errors, String, (String, String)] =
      Check(_.split('@') match {
        case Array(name, domain) =>
          (name, domain).validNel[String]
        case _ =>
          "Must contain a single @ character".
            invalidNel[(String, String)]
      })

    val checkLeft: Check[Errors, String, String] =
      Check(longerThan(0))
    val checkRight: Check[Errors, String, String] =
      Check(longerThan(3) and contains('.'))

    val joinEmail: Check[Errors, (String, String), String] =
      Check { case (l, r) =>
        (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
      }
    val checkEmail: Check[Errors, String, String] =
      splitEmail andThen joinEmail

    final case class User(username: String, email: String)
    def createUser(
                    username: String,
                    email: String): Validated[Errors, User] =
      (checkUsername(username), checkEmail(email)).mapN(User)

    val r = createUser("Noel", "noel@underscore.io")
    println(s"r is $r")

    val r2 = createUser("", "dave@underscore@io")
    println(s"r2 is $r2")

  }
}
