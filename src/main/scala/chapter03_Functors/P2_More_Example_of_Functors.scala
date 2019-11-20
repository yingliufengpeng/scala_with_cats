package chapter03_Functors

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object P2_More_Example_of_Functors {


  def main(args: Array[String]): Unit = {

    val futrue: Future[String] = Future(123).map(n => n + 1).map(n => n * 2).map(n => n + "!")

    val r = Await.result(futrue, 3.second)
    println(s"r is $r")

    /**
     * Futures and Referen?al Transparency
     * Note that Scala’s Futures aren’t a great example of pure functional pro-
     * gramming because they aren’t  referentially transparent. Future always
     * computes and caches a result and there’s no way for us to tweak this
     * behaviour. This means we can get unpredictable results when we use
     * Future to wrap side-effecting computations. For example:
     */


    val future1 = {
      // Initialize Random with a fixed seed:
      val r = new Random(0L)
      // nextInt has the side-effect of moving to
      // the next random number in the sequence:
      val x = Future(r.nextInt)
      // 这种写法是flatMap与map的语法糖的写法的过程
      for {
        a <- x
        b <- x
      } yield (a, b)
    }
    val future2 = {
      val r = new Random(0L)
      for {
        a <- Future(r.nextInt)
        b <- Future(r.nextInt)
      } yield (a, b)
    }


    val result1 = Await.result(future1, 1.second)
    // result1: (Int, Int) = (-1155484576,-1155484576)
    val result2 = Await.result(future2, 1.second)
    // result2: (Int, Int) = (-1155484576,-723955400)

    println(s"result1 is $result1")
    println(s"result2 is $result2")

    import cats.Monoid
    import cats.instances.function._  // for Functor
    import cats.syntax.functor._      // for map

    val func1: Int => Double = x => x.toDouble
    val func2: Double => Double = y => y * 2

    val r2 = (func1 map func2)(1)
    println(s"r is $r2")

    val r3 = (func1 andThen func2)(1)
    println(s"r3 is $r3")

    val r4 = func2(func1(1))
    println(s"r4 is $r4")

    val func = ((x: Int) => x.toDouble).map(n => n + 1).map(n => n * 2).map(n => n + "!")
    val r5 = func(1)
    println(s"r5 is $r5")


  }
}
