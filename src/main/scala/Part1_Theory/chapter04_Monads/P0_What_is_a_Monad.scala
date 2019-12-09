package Part1_Theory.chapter04_Monads

import scala.util.Try

object P0_What_is_a_Monad {
  def main(args: Array[String]): Unit = {

    //    def parseInt(str: String): Option[Int] = Try(str.toInt).toOption
    //
    //    def divide(a: Int, b: Int): Option[Int] = if (b == 0) None else Some(a / b)
    //
    //    // 这种语法自己需要好好的想一想,思考一下或许有不同的感觉
    //    def stringDivideBy(asStr: String, bStr: String): Option[Int] =
    //      parseInt(asStr).flatMap{ aNum =>
    //        parseInt(bStr).flatMap{ bNum =>
    //          divide(aNum, bNum)  // 调用参数已经深深的位于其中的逻辑的情况!!!
    //        }
    //      }
    //
    //    def stringDivideBy2(asStr: String, bStr: String): Option[Int] =
    //      for {
    //        aNum <- parseInt(asStr)
    //        bNum <- parseInt(bStr)
    //        ans <- divide(aNum, bNum)
    //      } yield ans
    //
    //    val r = stringDivideBy2("6", "2")
    //    println(s"r is $r")
    //
    //    val r2 = stringDivideBy("6", "0")
    //    println(s"r2 is $r2")
    //
    //    val r3 = stringDivideBy("6", "foo")
    //    println(s"r3 is $r3")
    //
    //    val r4 = stringDivideBy("bar", "2")
    //    println(s"r4 is $r4")

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits._
    import scala.concurrent.duration._

    def doSomethingLongRunning: Future[Int] = ???

    def doSomethingElseLongRunning: Future[Int] = ???

    def doSomethingVeryLongRunning: Future[Int] =
      for {
      result1 <- doSomethingLongRunning
      result2 <- doSomethingElseLongRunning
    } yield result1 + result2


    def doSomethingVeryLongRunning2: Future[Int] =
      doSomethingLongRunning.flatMap(result1 => {
        doSomethingElseLongRunning.map(result2 => {
          result1 + result2
        })
      })


  }
}
