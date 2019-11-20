package chapter04_Monads

object P6_The_Eval_Monad {

  def main(args: Array[String]): Unit = {
    import cats.Eval
    val now = Eval.now(math.random() + 1000)
    println(s"now is ${now.value}")

    val later = Eval.later(math.random() + 2000)
    println(s"later is ${later.value}")

    val always = Eval.always(math.random() + 3000)
    println(s"always is ${always.value}")

    val greeting = Eval.always{println("Step 1"); "hello"}
      .map{str => println("Step 2"); s"$str word"}

    println(s"greeting.value is ${greeting.value}")
    println(s"greeting.value is ${greeting.value}")
    println("--" * 20)
    val ans = for {
      a <- Eval.now{println("Calculating A"); 40}
      b <- Eval.always{println("Calculating B"); 2}
    } yield {
      println("Adding A and B")
      a + b
    }

//    println("ans begin")
    println(s"ans is ${ans.value}")
    println(s"ans is ${ans.value}")

    println("saying" + "-" * 20)
    val saying = Eval.always{println("Step 1"); "The Cat"}
      .map(str => {println("Step2"); s"$str sat on"})
      .memoize
      .map(str => {println("Step 3"); s"$str the mat"})

    println(s"saying.value is ${saying.value}")
    println(s"saying.value is ${saying.value}")

    def factorial(n: BigInt): BigInt =
      if(n == 1) n else n * factorial(n - 1)

//    val r = factorial(50000)
//    println(s"r is $r")

    def factorial2(n: BigInt): Eval[BigInt] =
      if (n == 1)
        Eval.now(n)
      else
        Eval.defer(factorial2(n - 1)).map(_ * n)

    val r2 = factorial2(500)
    println(s"r2 is ${r2.value}")

    def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
      as match {
        case head :: tail =>
          fn(head, foldRight(tail, acc)(fn))
        case Nil =>
          acc
      }

//    val r3 = foldRight((1 to 10000).toList, 0)(_ + _ )
//    println(s"r3 is $r3")

    def foldRight2[A, B](as: List[A], acc: B)(fn: (A, B) => B): Eval[B] =
      as match {
        case head :: tail =>
          Eval.defer(foldRight2(tail, acc)(fn).map(fn(head, _)))
        case Nil => Eval.later(acc)
      }

    val r4 = foldRight2((1 to 3330).toList, BigInt(0))(_ + _ )
    println(s"r4 is ${r4.value}")
  }

}
