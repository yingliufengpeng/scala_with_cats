package Part3_Solutions_to_Execises.P10_Data_Validation

object P5_Kleisli {

  import cats.Semigroup
  import cats.data.Validated
  import cats.syntax.semigroup._ // for |+|
  import cats.syntax.validated._
  import cats.data.Validated._ // for Valid and Invalid
  import cats.data.Kleisli
  import cats.data.NonEmptyList
  import cats.syntax.apply._ // for mapN
  import cats.instances.either._

  type Errors = NonEmptyList[String]

  type Result[A] = Either[Errors, A]
  type Check[A, B] = Kleisli[Either[Errors, ?], A, B]
  // Create a check from a function:
  def check[A, B](func: A => Result[B]): Check[A, B] =
    Kleisli(func)
  // Create a check from a Predicate:
  def checkPred[A](pred: Predicate[Errors, A]): Check[A, A] =
    Kleisli[Result, A, A](pred.run)

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

  val checkUsername: Check[String, String] =
    checkPred(longerThan(3) and alphanumeric)
  val splitEmail: Check[String, (String, String)] =
    check(_.split('@') match {
      case Array(name, domain) =>
        Right((name, domain))
      case other =>
        Left(error("Must contain a single @ character"))
    })
  val checkLeft: Check[String, String] =
    checkPred(longerThan(0))
  val checkRight: Check[String, String] =
    checkPred(longerThan(3) and contains('.'))
  val joinEmail: Check[(String, String), String] =
    check {
      case (l, r) =>
        (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
    }
  val checkEmail: Check[String, String] =
    splitEmail andThen joinEmail

  final case class User(username: String, email: String)
  def createUser(
                  username: String,
                  email: String): Either[Errors, User] = (
    checkUsername.run(username),
    checkEmail.run(email)
    ).mapN(User)

  def main(args: Array[String]): Unit = {

    val r = createUser("Noel", "noel@underscore.io")
    println(s"r is $r")

    val r2 = createUser("", "dave@underscore@io")
    println(s"r2 is $r2")
  }


  sealed trait Predicate[E, A] {

    import Predicate._

    def run(implicit s: Semigroup[E]): A => Either[E, A] =
      (a: A) => this(a).toEither

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

}
