package Part3_Solutions_to_Execises.P7_Foldable_and_Traverse

object P3_Traversing_with_Vectors {

  def main(args: Array[String]): Unit = {

    import cats.Applicative
    import cats.syntax.apply._ // for map
    import cats.syntax.applicative._ // for map

    def listTraverse[F[_] : Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(List.empty[B].pure[F])((accum, item) => (accum, func(item)).mapN(_ :+ _))

    def listSequence[F[_] : Applicative, B](list: List[F[B]]): F[List[B]] =
      listTraverse(list)(identity)

    import cats.instances.vector._

    val r = listSequence(List(Vector(1, 2, 3), Vector(4, 5, 6)))
    println(s"r is $r")

    val r2 = listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6)))
    println(s"r2 is $r2")

    import cats.instances.option._
    def process(inputs: List[Int]): Option[List[Int]] =
      listTraverse(inputs)(Option(_).filter(_ % 2 == 0))

    val r3 = process(List(2, 4, 6))
    println(s"r3 is $r3")

    import cats.data.Validated
    import cats.instances.list._ // for Monoid

    type ErrorOr[A] = Validated[List[String], A]

    def process2(inputs: List[Int]): ErrorOr[List[Int]] =
      listTraverse(inputs) { n =>
        if (n % 2 == 0)
          Validated.valid(n)
        else
          Validated.invalid(List(s"$n is not even"))
      }

    import cats.syntax.option._

    def process3(inputs: List[Int]): ErrorOr[List[Int]] =
      listTraverse(inputs)(e => Option(e).filter(_ % 2 == 0).toValid(List(s"$e is not even")))

    val r4 = process2(List(2, 4, 6))
    println(s"r4 is $r4")

    val r5 = process3(List(2, 4, 6))
    println(s"r5 is $r5")

    val r6 = process3(List(1, 4, 7))
    println(s"r5 is $r6")

  }

}
