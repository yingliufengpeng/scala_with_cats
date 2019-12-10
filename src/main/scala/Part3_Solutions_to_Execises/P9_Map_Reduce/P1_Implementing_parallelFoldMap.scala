package Part3_Solutions_to_Execises.P9_Map_Reduce



object P1_Implementing_parallelFoldMap {

  def main(args: Array[String]): Unit = {

    import scala.concurrent.Await
    import scala.concurrent.duration._
    import cats.Monoid
    import scala.concurrent.Future
    import cats.syntax.semigroup._ // for |+|
    import scala.concurrent.ExecutionContext.Implicits.global

    def foldMap[A, B](as: Vector[A])(f: A => B)(implicit monoid: Monoid[B]): B =
      as.foldLeft(monoid.empty)((acc, a) => monoid.combine(acc, f(a)))

    def parallelFoldMap[A, B: Monoid](as: Vector[A])(f: A => B): Future[B] = {
      // Calculate the number of items to pass each CPU
      val numCores = Runtime.getRuntime.availableProcessors()
      val groupSize = (1.0 * as.size / numCores).ceil.toInt

      // Create one group for each CPU

      val groups: Iterator[Vector[A]] = as.grouped(groupSize)

      // Create a future to foldMap each group
//      val futures: Iterator[Future[B]] = groups.map{ group =>
//        Future {
//          group.foldLeft(Monoid[B].empty)(_ |+| f(_))
//        }
//      }

      val futures2: Iterator[Future[B]] = groups.map(group => Future(foldMap(group)(f)))

      // foldMap over the groups to calculate a final result
      Future.sequence(futures2).map(iter => iter.foldLeft(Monoid[B].empty)(_ |+| _ ))

    }

    import cats.instances.int._

    val result = parallelFoldMap((1 to 10000).toVector)(identity)

    val r = Await.result(result, 1.second)

    println(s"r is $r")

  }
}
