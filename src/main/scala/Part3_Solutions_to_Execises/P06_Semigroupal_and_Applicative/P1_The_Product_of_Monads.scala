package Part3_Solutions_to_Execises.P06_Semigroupal_and_Applicative

object P1_The_Product_of_Monads {

  def main(args: Array[String]): Unit = {
    import cats.Monad
    import cats.syntax.flatMap._ // for flatMap
    import cats.syntax.functor._ // for map

    def product[F[_] : Monad, A, B](fa: F[A], fb: F[B]): F[(A, B)] = for {
      a <- fa
      b <- fb
    } yield (a, b)

    import cats.instances.list._ // for Semigroual
    val r = product(List(1, 2), List(3, 4))
    println(s"r is $r")

    import cats.instances.either._ // for Semigroupal

    type ErrorOr[A] = Either[Vector[String], A]

    // 使用kind-project插件提供的语法糖
    val r2 = product[Either[Vector[String], ?], Int, Int](
      //      Either.cond(test = false, 0, VectTraversableOnceor("Error1")),
      //      Either.cond(test = false, 0, Vector("Error2")),
      Left(Vector("Error1")),
      Left(Vector("Error2"))
    )
    println(s"r2 is $r2")


  }
}
