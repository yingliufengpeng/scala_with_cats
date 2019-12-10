package Part3_Solutions_to_Execises.P10_Data_Validation

import javax.swing.event.DocumentEvent.ElementChange

object P2_Basic_Combinators {
  import cats.Semigroup
  import cats.syntax.either._
  import cats.syntax.semigroup._ // for |+|


  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def apply(a: A)(implicit semigroup: Semigroup[E]): Either[E, A] =
      this match {
        case Pure(func) => func(a)
        case And(left, right) =>
          (left(a), right(a)) match {
            case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
            case (Left(e), Right(_)) => e.asLeft
            case (Right(_), Left(e)) => e.asLeft
            case _ => a.asRight
          }

      }

  }

  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]

  final case class Pure[E, A] (func: A => Either[E, A]) extends Check[E, A]

  def main(args: Array[String]): Unit = {




    val a: Check[List[String], Int] =
      Pure { v =>
        if(v > 2) v.asRight
        else List("Must be > 2").asLeft
      }
    val b: Check[List[String], Int] =
      Pure { v =>
        if(v < -2) v.asRight
        else List("Must be < -2").asLeft
      }
    val check: Check[List[String], Int] =
      a and b

    import cats.instances.list._

    val r3 = check(5)
    println(s"r3 is $r3")

    val r4 = check(4)
    println(s"r4 is $r4")

  }
}
