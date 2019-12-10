package Part3_Solutions_to_Execises.P07_Foldable_and_Traverse

object P2_Scaf_folding_Other_Methods {

  def main(args: Array[String]): Unit = {

    def map[A, B](list: List[A])(func: A => B): List[B] =
      list.foldRight(List.empty[B])((item, acc) => func(item) :: acc)

    def flatMap[A, B](list: List[A])(func: A => List[B]): List[B] =
      list.foldRight(List.empty[B])((item, acc) => func(item) ::: acc )

    val r = flatMap((1 to 10).toList)(e => List(1, 10, 100).map(_ * e))
    println(s"r is $r")

    def filter[A](list: List[A])(func: A => Boolean): List[A] =
      list.foldRight(List.empty[A])((item, acc) => {
        if (func(item)) item :: acc else acc
      })

    val r2 = filter((1 to 10).toList)(_ % 2 == 1)
    println(s"r2 is $r2")

    import scala.math.Numeric

    def sumWithNumeric[A](list: List[A])(implicit numeric: Numeric[A]): A =
      list.foldRight(numeric.zero)(numeric.plus)

    val r3 = sumWithNumeric((1 to 10).toList)
    println(s"r3 is $r3")

    import cats.Monoid

    def sumWithMonoid[A](list: List[A])(implicit monoid: Monoid[A]): A =
      list.foldRight(monoid.empty)(monoid.combine)

    import cats.instances.int._ // for Monoid

    val r4 = sumWithMonoid((1 to 10).toList)
    println(s"r3 is $r4")
  }

}
