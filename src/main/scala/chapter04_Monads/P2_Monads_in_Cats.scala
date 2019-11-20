package chapter04_Monads

object P2_Monads_in_Cats {

  def main(args: Array[String]): Unit = {

    import cats.Monad
    import cats.instances.option._ // for Monad
    import cats.instances.list._    // for Monad

    val opt1 = Monad[Option].pure(3)
    println(s"opt1 is $opt1")

    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    println(s"opt2 is $opt2")

    val opt3 = Monad[Option].map(opt2)(a => a * 10)
    println(s"opt3 is $opt3")

    val list1 = Monad[List].pure(3)
    println(s"list1 is $list1")

    val list2 = Monad[List].flatMap((1 to 3).toList)(a => List(a, a * 10))
    println(s"list2 is $list2")

    val list3 = Monad[List].map(list2)(a => a + 123)
    println(s"list3 is $list3")

    import cats.instances.vector._
    println(s"vector is ${Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10))}")

    import cats.instances.future._
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits._
    val fm = Monad[Future]
    val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
    val r = Await.result(future, 1.second)
    println(s"r is $r")

    import cats.instances.option._ // for Monad
    import cats.instances.list._   // for Monad
    import cats.syntax.applicative._ // for pure

    val r2 = 1.pure[Option]
    println(s"r2 is $r2")

    val r3 = 1.pure[List]
    println(s"r3 is $r3")

    import cats.Monad
    import cats.syntax.functor._  // for map
    import cats.syntax.flatMap._  // for flatMap

//    def sumSequre[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = a.flatMap(x => b.map(y => x * x + y * y))
    def sumSequre[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
      for {
        x <- a
        y <- b
      } yield x * x + y * y

    import cats.instances.option._      // for Monad
    import cats.instances.list._        // for Monad

    val r4 = sumSequre(Option(3), Option(4))
    println(s"r4 is $r4")

    val r5 = sumSequre(List(1, 2, 3, 4), List(5, 6))
    println(s"r5 is $r5")


  }
}
