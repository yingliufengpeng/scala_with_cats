package chapter01_Introduction
import cats.Show  // Trait 接口

object P4_Meet_Cats {

  def main(args: Array[String]): Unit = {

    // 1.4.2
//    import cats.instances.int._
//    val showInt: Show[Int] = Show.apply[Int]
//
//    import cats.instances.string._
//    val showString: Show[String] = Show.apply[String]
//
//    val r1 = showInt.show(44)
//    val r2 = showString.show("hello, cats")
//    println(s"r1 $r1, r2 $r2")


//    // 1.4.3
//    import cats.instances.int._
//    import cats.instances.string._
//    import cats.syntax.show._
//
//    val showInt = 123.show
//    val showString = "abd".show
//
//    println(s"r1 $showInt r2 $showString")

    // 1.4.5 Defining Custom Instance
    import java.util.Date
//    implicit val dateShow: Show[Date] = new Show[Date] {
//      override def show(t: Date): String = s"${t.getTime}ms since the epoch"
//    }
//
    implicit val dateShow: Show[Date] = Show.show[Date](date => s"${date.getTime}ms since the epoch")






  }
}
