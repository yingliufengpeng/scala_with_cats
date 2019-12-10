package Part3_Solutions_to_Execises.P10_Data_Validation

import cats.data.Validated.{Invalid, Valid}


object P3_Basic_Combinators2 {

  import cats.Semigroup
  import cats.data.Validated
  import cats.syntax.validated._
  import cats.syntax.semigroup._
  import cats.syntax.apply._ // for mapN
  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] =
      And(this, that)

    def or(that: Check[E, A]): Check[E, A] = Or(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
      this match {
        case Pure(func) =>
          func(a)
        case And(left, right) =>
          (left(a), right(a)).mapN((_, _) => a)
        case Or(left, right) =>
          left(a) match {
            case Valid(a) => Valid(a)
            case Invalid(e1) => right(a) match {
              case Valid(a) => Valid(a)
              case Invalid(e2) => (e1 |+| e2).invalid
            }
          }

      }
  }
  final case class And[E, A](
                              left: Check[E, A],
                              right: Check[E, A]) extends Check[E, A]

  final case class Or[E, A](
                              left: Check[E, A],
                              right: Check[E, A]) extends Check[E, A]
  final case class Pure[E, A](
                               func: A => Validated[E, A]) extends Check[E, A]

  def main(args: Array[String]): Unit = {
    val a: Check[List[String], Int] =
      Pure { v =>
        if(v > 2) v.valid
        else List("Must be > 2").invalid
      }
    val b: Check[List[String], Int] =
      Pure { v =>
        if(v < -2) v.valid
        else List("Must be < -2").invalid
      }
    val check: Check[List[String], Int] =
      a and b

    import cats.instances.list._

    val r3 = check(1)
    println(s"r3 is $r3")

    val r4 = check(4)
    println(s"r4 is $r4")

    val checkor: Check[List[String], Int] = a or b
    val r5 = checkor(-3)
    println(s"r5 is $r5")

  }
}
