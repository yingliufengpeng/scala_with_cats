package Part2_Case_Studies.chapter02_Case_Study_Map_Reduce

object P1_Implementing_foldMap {


  def main(args: Array[String]): Unit = {

    import cats.Monoid
    import cats.syntax.semigroup._ // for |+|

    def foldMap[A, B](as: Vector[A])(f: A => B)(implicit monoid: Monoid[B]): B =
      as.foldLeft(monoid.empty)((acc, a) => monoid.combine(acc, f(a)))

    def foldMap2[A, B](as: Vector[A])(f: A => B)(implicit monoid: Monoid[B]): B =
      as.foldLeft(monoid.empty)((acc, a) => acc |+| f(a))

    import cats.instances.int._ // for Monoid
    val r = foldMap(Vector(1, 2, 3))(identity)
    println(s"r is $r")

    import cats.instances.string._ // for Monoid
    val r2 = foldMap(Vector(1, 2, 3))(_.toString + "!")
    println(s"r2 is $r2")

    val r3 = foldMap2("Hello world!".toVector)(_.toString.toUpperCase)
    println(s"r3 is $r3")
  }
}
