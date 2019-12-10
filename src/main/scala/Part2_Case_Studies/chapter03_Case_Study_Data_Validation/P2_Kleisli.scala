package Part2_Case_Studies.chapter03_Case_Study_Data_Validation

object P2_Kleisli {

  def main(args: Array[String]): Unit = {

    import cats.data.Kleisli
    import cats.instances.list._ // for Monad

    val step1: Kleisli[List, Int, Int] =
      Kleisli(x => List(x + 1, x - 1))

    val step2: Kleisli[List, Int, Int] =
      Kleisli(x => List(x, -x))

    val step3: Kleisli[List, Int, Int] =
      Kleisli(x => List(x * 2, x / 2))

    val pipline = step1 andThen step2 andThen step3
    val r = pipline.run(20)
    println(s"r is $r")

  }
}
