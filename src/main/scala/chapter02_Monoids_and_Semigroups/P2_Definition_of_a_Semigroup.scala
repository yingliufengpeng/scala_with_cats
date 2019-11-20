package chapter02_Monoids_and_Semigroups

object P2_Definition_of_a_Semigroup {

  // 半群
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  // 群目前这样去理解
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
  }

  def main(args: Array[String]): Unit = {


  }

}
