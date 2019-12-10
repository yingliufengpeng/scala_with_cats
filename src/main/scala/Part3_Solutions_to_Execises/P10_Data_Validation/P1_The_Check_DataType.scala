package Part3_Solutions_to_Execises.P10_Data_Validation

object P1_The_Check_DataType {

  def main(args: Array[String]): Unit = {

    //    type Check2[E, A] = A => Either[E, A]

    trait Check2[E, A] {
      def apply(value: A): Either[E, A]
    }

    import cats.Semigroup
    import cats.instances.list._ // for Semigroup
    import cats.syntax.semigroup._ // for |+|

    val semigroup = Semigroup[List[String]]
    val r = semigroup.combine(List("A"), List("B"))
    println(s"r is $r")

    val r2 = List("B") |+| List("C")
    println(s"r2 is $r2")


    import cats.Semigroup
    import cats.syntax.either._ // for asLeft and asRight
    import cats.syntax.semigroup._ // for |+|

    final case class CheckF[E: Semigroup, A](func: A => Either[E, A]) {
      def apply(value: A): Either[E, A] = func(value)

      def and(that: CheckF[E, A]): CheckF[E, A] =
        CheckF { a =>
          (this (a), that(a)) match {
            case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
            case (Left(e), Right(_)) => e.asLeft
            case (Right(_), Left(e)) => e.asLeft
            case _ => a.asRight
          }
        }
    }

    import cats.instances.list._ // for Semigroup

    val a = CheckF[List[String], Int]{ v =>
      if (v > 2) v.asRight
      else List("Must be > 2").asLeft
    }

    val b = CheckF[List[String], Int]{ v =>
      if (v < -2) v.asRight
      else List("Must be < -2").asLeft
    }

    val check: CheckF[List[String], Int] = a and b

    val r3 = check(5)
    println(s"r3 is $r3")

    val r4 = check(0)
    println(s"r4 is $r4")






  }
}
