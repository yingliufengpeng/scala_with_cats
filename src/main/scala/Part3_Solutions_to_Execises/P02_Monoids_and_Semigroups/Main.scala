package Part3_Solutions_to_Execises.P02_Monoids_and_Semigroups

import cats.Order

object Main {

  def main(args: Array[String]): Unit = {

    import cats.Monoid // for Trait Monoid
    import cats.instances.int._ //
    import cats.instances.string._

    implicit def setUnionMonid[A]: Monoid[Set[A]] = new Monoid[Set[A]] {
      override def empty: Set[A] = Set.empty[A]
      override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
    }

    val intSetMonoid = Monoid[Set[Int]]
    val strSetMonoid = Monoid[Set[String]]
    val r = intSetMonoid combine (Set(1, 2, 3), Set(4, 3, 1))
    println(s"r is $r")


    import cats.Monoid
    import cats.instances.int._ // for Monoid
    import cats.instances.option._ // for Monoid
    import cats.syntax.semigroup._ // for |+|
    import cats.syntax.option._

    def add[A: Monoid](items: List[A]): A = items.fold(Monoid[A].empty)(_ |+| _ )
    val r2 = add(List(3, 4, 5))
    println(s"r2 is $r2")

    val r3 = add(List(Option(4), Option(4)))
    println(s"r3 is $r3")
    case class Order(totalCost: Double, quantity: Double)
    implicit val monoid: Monoid[Order] = new Monoid[Order] {
      override def empty: Order = Order(0, 0)

      override def combine(o1: Order, o2: Order): Order = Order(
        o1.totalCost + o2.totalCost,
        o1.quantity + o2.quantity
      )
    }


  }

}
