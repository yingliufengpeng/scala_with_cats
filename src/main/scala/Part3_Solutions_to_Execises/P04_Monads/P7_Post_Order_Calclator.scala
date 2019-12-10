package Part3_Solutions_to_Execises.P04_Monads

import cats.data.State
import cats.syntax.applicative._ // for pure

object P7_Post_Order_Calclator {

  def main(args: Array[String]): Unit = {

    type CalcState[A] = State[List[Int], A]

    def operator(f: (Int, Int) => Int): CalcState[Int] =
      State[List[Int], Int] {
        case b :: a :: tail =>
          val ans = f(a, b)
          (ans :: tail, ans)
        case Nil =>
          sys.error("Fail")
      }

    def operand(num: Int): CalcState[Int] =
      State[List[Int], Int] (stack => (num :: stack, num ))

    def evalOne(sym: String): CalcState[Int] = sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }

    def evalAll(input: List[String]): CalcState[Int] =
      input.foldLeft(0.pure[CalcState])((cal, a) => {
        cal.flatMap(_ => evalOne(a)) // 使用到cal中的栈,否则使用下面的就会报错
//        evalOne(a)
      })

    def evalInput(input: String): Int = evalAll(input.split(" ").toList).runA(Nil).value

    val r = evalInput("1 2 + 3 4 + *")

    println(s"r is $r")
  }
}
