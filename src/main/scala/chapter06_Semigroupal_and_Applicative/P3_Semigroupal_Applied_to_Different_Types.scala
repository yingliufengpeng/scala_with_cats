package chapter06_Semigroupal_and_Applicative

import com.sun.org.apache.xpath.internal.functions.FuncTranslate

object P3_Semigroupal_Applied_to_Different_Types {


  def main(args: Array[String]): Unit = {

    import cats.Semigroupal
    import cats.instances.future._ // for Semigroupal
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    val futrePair = Semigroupal[Future].product(Future("Hello"), Future("World"))
    val r = Await.result(futrePair, 1.second)
    println(s"r is $r")

    import cats.syntax.apply._ // for manpN
    case class Cat(name: String, yearOfBirth: Int, favoriteFoods: List[String])

    val futureCat = (
      Future("Wang"),
      Future(1933),
      Future(List("KKK"))
    ).mapN(Cat)

    val r2 = Await.result(futureCat, 1.second)
    println(s"r2 is $r2")

    /**
     *  List
     * Combining Lists with Semigroupal produces some potentially unexpected
     * results. We might expect code lik ethe following to zip the lists, but we actually
     * get the cartesian product of their elements:
     */
    import cats.Semigroupal
    import cats.instances.list._    // for Semigroupal

    val r3 = Semigroupal[List].product(List(1, 2), List(3, 4))
    println(s"r3 is $r3")

    /**
     *  Either
     *
     *  We opened this chapter with a discussion of fail-fast versus accumulating
     *  error-handing. We might expect product applied to Either to accumulating
     *  the errors instead of fail fast. Again, perhaps surprisingly, we find product
     *  implements the same fail-fast behaviour as flatMap:
     *
     */
    import cats.instances.either._    // for Semigroupal
    import cats.instances.vector._ // for
    type ErrorOr[A] = Either[Vector[String], A]

    val r4 = Semigroupal[ErrorOr].product(
      Left(Vector("Error 2")),
      Left(Vector("Error 1")),
    )
    println(s"r4 is $r4")


    val a = Future("Future 1")
    val b = Future("Future 2")

    val c = for {
      x <- a
      y <- b
    } yield (x, y)

    println(s"c is ${Await.result(c, 1.second)}")

    import cats.Monad
    def product[M[_]: Monad, A, B](x: M[A], y: M[B]): M[(A, B)] = {
     import cats.syntax.applicative._
//      val m = x.pure[M[A]]
//      val n = y.pure[M[B]]
//      val c = m + n
//      for {
//        a <- x.pure
//        b <- y.pure
//      } yield (a, b)

      ??? // 该实现并没有思考情况到底该怎么走!!!
    }



  }
}
