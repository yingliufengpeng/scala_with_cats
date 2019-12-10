package Part3_Solutions_to_Execises.P11_CRDTs



object P2_Generalisation {


  def main(args: Array[String]): Unit = {
    import cats.kernel.CommutativeMonoid

    trait BoundedSemiLattice[A] extends CommutativeMonoid[A] {
      def combine(a1: A, a2: A): A
      def empty: A
    }

    object BoundedSemiLattice {

      implicit val intInstance: BoundedSemiLattice[Int] = new BoundedSemiLattice[Int] {
        override def combine(a1: Int, a2: Int): Int = a1 max a2

        override def empty: Int = 0
      }

      implicit def setInstance[A](): BoundedSemiLattice[Set[A]] = new BoundedSemiLattice[Set[A]] {
        override def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2

        override def empty: Set[A] = Set.empty[A]
      }
    }

    import cats.instances.list._ // for Monoid
    import cats.instances.map._  // for Monoid
    import cats.syntax.semigroup._ // for |+|
    import cats.syntax.foldable._   // for combineAll

    final case class GCounter[A](couters: Map[String, A]) {

      def increment(machine: String, amount: A)(implicit m: CommutativeMonoid[A]): GCounter[A] = {
        val value = amount |+| couters.getOrElse(machine, m.empty)
        GCounter(couters + (machine -> value))
      }

      def merge(that: GCounter[A])(implicit b: BoundedSemiLattice[A]): GCounter[A] =
        GCounter(this.couters |+| that.couters)

      def total(implicit m: CommutativeMonoid[A]): A =
        this.couters.values.toList.combineAll

    }

  }
}
