package Part3_Solutions_to_Execises.P07_Foldable_and_Traverse

object P1_Reflecting_on_Folds {

  def main(args: Array[String]): Unit = {

    val r = List(1, 2, 3).foldLeft(List.empty[Int])((acc, i) => i :: acc)

    val r2 = List(1, 2, 3).foldRight(List.empty[Int])((i, acc) => i :: acc )

    println(s"r is $r, r2 is $r2")

  }
}
