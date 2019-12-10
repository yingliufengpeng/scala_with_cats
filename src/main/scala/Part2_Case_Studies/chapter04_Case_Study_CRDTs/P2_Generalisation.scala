package Part2_Case_Studies.chapter04_Case_Study_CRDTs

object P2_Generalisation {

  def main(args: Array[String]): Unit = {
    import cats.kernel.CommutativeMonoid
    trait BoundedSemiLattice[A] extends CommutativeMonoid[A] {
      def combine(a1: A, a2: A): A
      def empty: A
    }

  }
}
