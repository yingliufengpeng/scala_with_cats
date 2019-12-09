package Part1_Theory.chapter03_Functors

object P1_Examples_of_Functors {


  def main(args: Array[String]): Unit = {
    val r = List(1, 2, 3).map(n => n + 1).map(n => n * 2).map(n => n + "!")
    println(s"r is $r")
  }
}
