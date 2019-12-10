package Part2_Case_Studies.chapter02_Case_Study_Map_Reduce

object P2_Parallelising_foldMap {

  def main(args: Array[String]): Unit = {

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    val future1 = Future{
      (1 to 100).toList.foldLeft(0)(_ + _)
    }

    val future2 = Future {
      (100 to 200).foldLeft(0)(_ + _)
    }

    val future3 = future1.map(_.toString)

    val future4 = for {
      a <- future1
      b <- future2
    } yield a + b


    val r = Future.sequence((1 to 30).map(Future(_)))

    import cats.syntax.traverse._ // for sequence
    import cats.instances.future._ // for Applicative
    import cats.instances.list._ // for Traverse

    val r2 = List(Future(1), Future(2)).sequence

    import scala.concurrent._
    import scala.concurrent.duration._

    Await.result(Future(1), 1.second)

    import cats.{Monoid, Monad}
    import cats.instances.int._ // for Monoid
    import cats.instances.future._ // for Monad and Monoid

    val r3 = Monad[Future].pure(43)

    val r4 = Monoid[Future[Int]].combine(Future(1), Future(2))

    /**
     * (1 to 10).toList.grouped(3).toList
     * // res16: List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7,
     * 8, 9), List(10))
     */




  }
}
