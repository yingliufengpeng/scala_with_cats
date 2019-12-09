package Part1_Theory.chapter03_Functors

object P4_Aside_Hihger_Kinds_and_Type_Constructor {


  def main(args: Array[String]): Unit = {

    val f = (x: Int) => x * 2
    val f2 = f andThen f
    val r = f2(4)
    println(s"r is $r")

  }
}
