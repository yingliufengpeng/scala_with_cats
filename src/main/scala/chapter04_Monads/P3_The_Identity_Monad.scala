package chapter04_Monads

object P3_The_Identity_Monad {
  def main(args: Array[String]): Unit = {

    import cats.Monad
    import cats.syntax.functor._  // for map
    import cats.syntax.flatMap._  // for flatMap
    import cats.Id

    //    def sumSequre[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = a.flatMap(x => b.map(y => x * x + y * y))
    def sumSequre[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
      for {
        x <- a
        y <- b
      } yield x * x + y * y

    val r = sumSequre(3: Id[Int], 4: Id[Int])
    println(s"r is $r")

    val a = Monad[Id].pure(3)
    println(s"a is $a")
    val b = Monad[Id].flatMap(a)(_ + 1)
    println(s"b is $b")

    import cats.syntax.functor._  // for map
    import cats.syntax.flatMap._  // for flatMap

//    a.flatMap(x => b.map(y => x + y))
    val r2 = for {
      x <- a
      y <- b
    } yield x + y

    println(s"r is $r2")
  }
}
