package chapter04_Monads

object P9_The_State_Monad {


  def main(args: Array[String]): Unit = {
    import cats.data.State // for State[S, A] ===> S => A

    val a = State[Int, String]{ state => (state, s"The state is $state")}
    val (state, result) = a.run(10).value
    println(s"state is $state, result is $result")

    val state2 = a.runS(10).value
    println(s"state2 is $state2")

    val result2 = a.runA(10).value
    println(s"result2 is $result2")

    val step1 = State[Int, String] { num =>
      val ans = num + 1
      (ans, s"Result of step1: $ans")
    }

    val step2 = State[Int, String] { num =>
      val ans = num + 2
      (ans, s"Result of step2: $ans")
    }

    val step3 = State[Int, String] { num =>
      val ans = num + 3
      (ans, s"Result of step2: $ans")
    }

    val both = for {
      a <- step1
      b <- step2
      c <- step3
    } yield (a, b, c)

    val (s1, r1) = both.run(20).value
    println(s"s1 is $s1, r1 is $r1")

    val getDemo = State.get[Int]
    println(s"getDemo ${getDemo.run(10).value}")

    val setDemo = State.set[Int](30)
    println(s"setDemo is ${setDemo.run(10).value}")

    val pureDemo = State.pure[Int, String]("Result")
    println(s"pureDemo is ${pureDemo.run(10).value}")

    val inspectDemo = State.inspect[Int, String](e => e + "!")
    println(s"inspectDemo is ${inspectDemo.run(10).value}")

    val modifyDemo = State.modify[Int](_ + 1)
    println(s"modifyDemo is ${modifyDemo.run(10).value}")

    import State._
    val program = for {
      a <- get[Int]                   // State[Int, Int]
      _ <- set[Int](a + 1)            // State[Int, Unit]
      b <- get[Int]                   // State[Int, Int]
      _ <- modify[Int](_ + 1)         // State[Int, Unit]
      c <- inspect[Int, Int](_ * 1000)  // State[Int, Int]
    } yield (a, b, c)

    println(s"program is ${program.run(10).value}")

    type CalcState[A] = State[List[Int], A]

    def someTransformation(oldStack: List[Int], new_value: String): List[Int] = {
      new_value match {
        case "+" | "*" =>
          val sec = oldStack.head
          val fir = oldStack.tail.head
          val last = oldStack.tail.tail
          if (new_value == "+")
            (fir + sec) :: last
          else
            (fir * sec):: last
        case _ =>     new_value.toInt :: oldStack
      }
    }

    def evalOne(sym: String): CalcState[Int] = State[List[Int], Int] { oldStack =>
      val newStack = someTransformation(oldStack, sym)
      val result = newStack.head
      (newStack, result)
    }

    val r = evalOne("42").runA(Nil).value
    println(s"r is $r")

    val program2 = for {
      _ <- evalOne("1")
      _ <- evalOne("2")
      ans <- evalOne("+")
    } yield ans

//    val r2 = program2.runA(Nil).value
//    println(s"r2 is $r2")

    def someTransformation2(oldStack: List[Int], input: List[String]): List[Int] = {

      ???
    }

    def evalAll(input: List[String]): CalcState[Int] = State[List[Int], Int] { oldStack =>
      val (nS, re) =  input.map(evalOne).reduce((e1, e2) => for {
        _ <- e1
        ans <- e2
      } yield ans).run(Nil).value
      (nS ++ oldStack, re)
    }

//    val program3 = evalAll(List("1", "2", "+", "3", "*")).runA(Nil).value
//    println(s"program3 is $program3")

    val program4 = for {
      _ <- evalAll(List("1", "2", "+"))
      _ <- evalAll(List("3", "4", "+"))
      ans <- evalOne("*")
    } yield ans
    println(s"program4 is ${program4.runA(Nil).value}")


  }
}
