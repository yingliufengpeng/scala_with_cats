package chapter02_Monoids_and_Semigroups

object P1_Definition_of_a_Monoid {

  trait Monoid[A] {
    def combine(x: A, y: A): A
    def empty: A
  }

  // 需要满足结合律
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
  }

  // 需要满足零率
  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.empty) == m.combine(m.empty, x) == x
  }

  def main(args: Array[String]): Unit = {

  }

}
