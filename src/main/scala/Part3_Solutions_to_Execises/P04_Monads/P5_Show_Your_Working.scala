package Part3_Solutions_to_Execises.P04_Monads

object P5_Show_Your_Working {

  def main(args: Array[String]): Unit = {

    import cats.data.Writer
    import cats.syntax.applicative._ // for pure
    import cats.instances.vector._ // for Monoid

    type Logged[A] = Writer[Vector[String], A]

    val r = 43.pure[Logged]
    println(s"r is $r")

    import cats.syntax.writer._ // for tell

    val r2 = Vector("Me").tell
    println(s"r2 is $r2")

    def slowly[A](body: => A) =
      try body finally Thread.sleep(100)

    // 这种的写法确实很有感觉的特色情况!!!
    def factorial(n: Int): Logged[Int] =
      for {
        ans <- if(n == 0) {
          1.pure[Logged]
        } else {
          slowly(factorial(n - 1).map(_ * n))
        }
        // 填写日志信息,这个可真是函数式的写法的特色
        _ <- Vector(s"fact $n $ans").tell
      } yield ans

    val r3 = factorial(5)
    println(s"r3 is $r3")

    import scala.concurrent._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    val r4 = Await.result(Future.sequence(Vector(
      Future(factorial(3)),
      Future(factorial(3))
    )), 5.seconds)

    println(s"r4 is $r4")
  }
}
