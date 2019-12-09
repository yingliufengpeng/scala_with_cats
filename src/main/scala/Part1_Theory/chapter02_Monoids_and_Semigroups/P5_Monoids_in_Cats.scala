package Part1_Theory.chapter02_Monoids_and_Semigroups

object P5_Monoids_in_Cats {

  def main(args: Array[String]): Unit = {
    import cats.Monoid
    import cats.Semigroup
    import cats.instances.string._ // for Monoid
    val r = Monoid[String].combine("Hi", "there")
    println(s"r is $r")

    val r2 = Monoid[String].empty
    println(s"r2 is $r2")

    val r3 = Semigroup[String].combine("wang", "peng")
    println(s"r3 is $r3")

    import cats.instances.int._ // for Monoid
    val r4 = Monoid[Int].combine(4, 5)
    println(s"r4 is $r4")

    import cats.instances.option._
    val r5 = Monoid[Option[Int]].combine(Some(4), Some(5))
    println(s"r5 is $r5")

    import cats.syntax.semigroup._  // for |+|
    val stringResult = "Hi" |+| "there" |+| Monoid[String].empty
    println(s"stringResult is $stringResult")

    import cats.instances.int._ // for Monoid
    val intResult = 1 |+| 2 |+| Monoid[Int].empty
    println(s"intResult is $intResult")

    implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
      override def empty: Order = Order(0, 0)

      override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
    }


    val r6 = Order(3, 4) |+| Order(4, 5) |+| Monoid[Order].empty
    println(s"r6 is $r6")


  }

  case class Order(totalCost: Double, quantity: Double)

}
