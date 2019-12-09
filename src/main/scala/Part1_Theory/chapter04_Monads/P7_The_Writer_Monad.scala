package Part1_Theory.chapter04_Monads

object P7_The_Writer_Monad {
  def main(args: Array[String]): Unit = {
    import cats.data.Writer
    import cats.instances.vector._ // for Monoid

    val r = Writer(Vector("It was the best times", "It was the worst times"), 1859)
    println(s"r is $r")

    import cats.syntax.applicative._ // for pure
    type Logged[A] = Writer[Vector[String], A]

    val r2 = 123.pure[Logged]
    println(s"r2 is $r2")

    import cats.syntax.writer._ // for tell
    val r3 = Vector("msg1", "msg2", "msg3").tell
    println(s"r3 is $r3")

    import cats.syntax.writer._ // for writer
    val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
    println(s"a is $a")

    val b = 123.writer(Vector("msg1", "msg2", "msg3"))
    println(s"b is $b")

    val aResult: Int = a.value
    val aLog: Vector[String] = a.written
    println(s"aResult $aResult aLog $aLog")

    val writer1 = for {
      a <- 10.pure[Logged]
      _ <- Vector("a", "b", "c").tell
      b <- 32.writer(Vector("x", "y", "z"))
    } yield a + b
    println(s"write1 is $writer1")
    println(s"write1'run is ${writer1.run}")

    val writer2 = writer1.mapWritten(e =>e.map(_.toUpperCase))
    println(s"writer2 is $writer2")

    val writer3 = writer1.bimap(
      log => log.map(_.toUpperCase),
      res => res * 100
    )
    println(s"writer3 is $writer3")

    val writer4 = writer1.mapBoth((log, res) =>  (log.map(_ + "!"), res * 1000))
    println(s"writer4 is $writer4")

    val writer5 = writer1.reset
    println(s"writer5 is $writer5")

    val writer6 = writer1.swap
    println(s"writer6 is $writer6")

    def slowly[A](body: => A): A =
      try body finally Thread.sleep(100)
    def factorial(n: Int): Int = {
      val ans = slowly(if(n == 0) 1 else n * factorial(n - 1))
      println(s"fact $n $ans")
      ans
    }

//    val r4 = factorial(15)
//    println(s"r4 is $r4")

    import scala.concurrent._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    Await.result(Future.sequence(Vector(
      Future(factorial(3)),
      Future(factorial(3)),
      Future(factorial(3)),
      Future(factorial(3)),
    )), 5.seconds)

  }
}
