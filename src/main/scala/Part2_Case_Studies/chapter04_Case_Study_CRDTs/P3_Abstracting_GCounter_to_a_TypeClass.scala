package Part2_Case_Studies.chapter04_Case_Study_CRDTs

object P3_Abstracting_GCounter_to_a_TypeClass {

  def main(args: Array[String]): Unit = {

    import cats.kernel.CommutativeMonoid
    import cats.syntax.semigroup._    // for |+|
    import cats.syntax.foldable._     // for combineAll
    import cats.instances.list._      // for Monoid
    import cats.instances.map._       // for Monoid

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

    trait GCounter[F[_, _], K, V] {

      def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V]

      def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V]

      def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V
    }

    object GCounter {
      implicit def mapInstance[K, V]: GCounter[Map, K, V] = new GCounter[Map, K, V] {
        override def increment(map: Map[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): Map[K, V] = {
          val total = map.getOrElse(k, m.empty) |+| v
          map + (k -> total)
        }

        override def merge(map1: Map[K, V], map2: Map[K, V])(implicit b: BoundedSemiLattice[V]): Map[K, V] =
          map1 |+| map2

        override def total(map: Map[K, V])(implicit m: CommutativeMonoid[V]): V =
          map.values.toList.combineAll
      }
    }
  }

}
