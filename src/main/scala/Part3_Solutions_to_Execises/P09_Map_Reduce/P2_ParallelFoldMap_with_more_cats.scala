package Part3_Solutions_to_Execises.P09_Map_Reduce

object P2_ParallelFoldMap_with_more_cats {

  def main(args: Array[String]): Unit = {
    import cats.Monoid
    import cats.instances.int._ // for Monoid
    import cats.instances.future._ // for Applicative and Monad
    import cats.instances.vector._ // for Foldable and Traverse
    import cats.syntax.foldable._ // for combineAll and foldMap
    import cats.syntax.traverse._ // for traverse
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global


    def parallelFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] = {
      val numCores = Runtime.getRuntime.availableProcessors
      val groupSize = (1.0 * values.size / numCores).ceil.toInt
      values
        .grouped(groupSize)
        .toVector
        .traverse(group => Future(group.foldMap(func)))
        .map(_.combineAll)
    }

    val future = parallelFoldMap((1 to 100).toVector)(_ * 100)
    val r = Await.result(future, 1.second)
    println(s"r is $r")


  }
}
