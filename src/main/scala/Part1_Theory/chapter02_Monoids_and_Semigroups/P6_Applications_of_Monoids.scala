package Part1_Theory.chapter02_Monoids_and_Semigroups

import cats.Monoid
import cats.syntax.semigroup._

object P6_Applications_of_Monoids {


  def main(args: Array[String]): Unit = {
    import cats.Monoid
    import cats.instances.string._  // for Monoid
    import cats.syntax.semigroup._  // for |+|

    val r = "Scala" |+| "with" |+| "Cats"
    println(s"r is $r")

    import  cats.instances.int._
    import cats.instances.option._ // for Monoid
    val r2 = Option(1) |+| Option(2)
    println(s"r2 is $r2")

    import cats.instances.map._
    val map1 = Map("a" -> 1, "b" -> 2)
    val map2 = Map("b" -> 3, "d" -> 4)
    val r3 = map1 |+| map2
    println(s"r3 is $r3")

    import  cats.instances.tuple._ // for Monoid
    val tuple1 = ("Hello", 123)
    val tuple2 = ("World", 321)
    val r4 = tuple1 |+| tuple2
    println(s"r4 is $r4")

    val r5 = addAll(List(1, 2, 3))
    println(s"r5 is $r5")

    val r6 = List(Some(1), Some(2), None)
    println(s"r6 is ${addAll(r6)}")

//    println(s"${Option(1) |+| Option(2) |+| Option.empty[Int]}")
//    println(s"${addAll(List[Option[Int]](Some(1), Some(2)))}")

  }

  def addAll[A](values: List[A])(implicit monoid: Monoid[A]): A = values.foldRight(monoid.empty)(_ |+| _)
}
