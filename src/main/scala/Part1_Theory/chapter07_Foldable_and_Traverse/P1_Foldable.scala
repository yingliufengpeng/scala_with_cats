package Part1_Theory.chapter07_Foldable_and_Traverse

object P1_Foldable {

  def main(args: Array[String]): Unit = {

    def show[A](list: List[A]): String =
      list.foldLeft("nil")((accum, item) => s"$item then $accum")

    val r = show(Nil)
    println(s"r is $r")

    val r2 = show(List(1, 2, 3, 4))
    println(s"r2 is $r2")

    val r3 = (1 to 4).foldLeft(0)(_ + _)
    val r4 = (1 to 4).foldRight(0)(_ + _)
    println(s"r3 is $r3, r4 is $r4")

    val r5 = List(1, 2, 3).foldLeft(0)(_ - _)
    val r6 = List(1, 2, 3).foldRight(0)(_ - _)
    println(s"r5 is $r5, r6 is $r6")

    val list = (1 to 4).toList
    val r7 = list.foldLeft(List.empty[Int])((res, e) => e :: res)
    println(s"r7 is $r7")

    val r8 = list.foldRight(List.empty[Int])((e, res) => e :: res)
    println(s"r7 is $r8")

    def map2(list: List[Int])(f: Int => Int): List[Int] =
      list.foldRight[List[Int]](Nil)((e, res) => f(e):: res)

    val r9 = map2(List(1, 2, 3, 4))(_ * 2)
    val r10 = List(1, 2, 3, 4).map(_ * 2)
    println(s"r9 == r10 ${r9 == r10}")

    import cats.Foldable
    import cats.instances.list._ // for Foldable

    val ints = List(1, 2, 3)
    val r11 = Foldable[List].foldLeft(ints, 0)(_ + _)
    println(s"r11 is $r11")

    import cats.instances.option._  // for Foldable
    val maybeInt = Option(123)
    val r12 = Foldable[Option].foldLeft(maybeInt, 10)(_ * _)
    println(s"r12 is $r12")

    /**
     * The default implementation of foldRight for Stream is not stack safe. The longer
     * stream, the larger the stack requirements for the fold. A sufficiently large
     * stream will trigger a StackOverflowError
     */

    import cats.Eval
    import cats.Foldable

    def bigData = (1 to 100000).toStream
//    val r13 = bigData.foldRight(0L)(_ + _)
//    println(s"r13 is $r13")

    import cats.instances.stream._    // for Foldable
    val eval: Eval[Long] = Foldable[Stream]
      .foldRight(bigData, Eval.now(0L)){ (num, eval) => {
        eval.map(_ + num)
      }}
    println(s"eval's value is ${eval.value}")

    val r14 = Foldable[Option].nonEmpty(Option(43))
    println(s"r14 is $r14")

    val r15 = Foldable[List].find(List(1, 2, 3, 4))(_ % 2 == 0 )
    println(s"r15 is $r15")

    /**
     *  In addition to these familar methods, Cats provides two methods that
     *  make use of Monoids:
     *
     *    combineAll(ans its alias fold) combines all elements in the sequence
     *      using their Monoid
     *    foldMap maps a user-supplied function over the sequence and combines
     *      the results using a Monoid
     *
     */

    import cats.instances.int._   // for Monoid
    val r16 = Foldable[List].combineAll(List(1, 2, 3))
    println(s"r16 is $r16")

    import cats.instances.string._ // for Monoid
    val r17 = Foldable[List].foldMap(List(1, 2, 3))(_.toString)
    println(s"r17 is $r17")

    /**
     *  Finally, we can compose Foldables to support deep traversal of nested
     *  sequences
     */
    import cats.instances.vector._ // for Monoid
    val ints2 = List(Vector(1, 2, 3), Vector(4, 5, 6))
    val r18 = (Foldable[List] compose Foldable[Vector]).combineAll(ints2)
    println(s"r18 is $r18")

    // syntax for foldable
    import cats.syntax.foldable._ // for combineAll and foldMap
    val r19 = List(1, 2, 3).combineAll
    println(s"r19 is $r19")
    val r20 = List(1, 2, 3).foldMap(_.toString)
    println(s"r20 is $r20")

    def sum[F[_]: Foldable](values: F[Int]): Int =
      values.foldLeft(0)(_ + _)

    val r21 = sum(List(1, 2, 3))
    println(s"r21 is $r21")



  }

}
