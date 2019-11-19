package chapter01_Introduction
import cats.Eq

object P5_Example_Eq {

  def main(args: Array[String]): Unit = {
    import cats.instances.int._
    val eqInt = Eq[Int]
    val r = eqInt.eqv(12, 12)
    val r2 = eqInt eqv(12, 13)
    println(s"r is $r, r2 is $r2")

    import cats.syntax.eq._ // for === and =!=
    val r3 = 123 === 123
    val r4 = 123 =!= 134
    println(s"r3 is $r3, r4 is $r4")


//    123 === "2323" // 出现这种类型不匹配的错误确实有一套

    import cats.instances.option._ // for Eq
//    Some(1) === None
    val r5 = (Some(1): Option[Int]) === (None: Option[Int])
    println(s"r5 is $r5")
    val r6 = Option(1) === Option.empty[Int]
    println(s"r6 is $r6")

    import cats.syntax.option._
    val r7 = 1.some === none[Int]
    println(s"r7 is $r7")
    val r8 = 1.some =!= none[Int]
    println(s"r8 is $r8")

    import java.util.Date
    import cats.instances.long._ // for Eq
    implicit val dateEq: Eq[Date] = Eq.instance[Date]((date1, date2) => date1.getTime === date2.getTime)
    val x = new Date
    val y = new Date
    val r9 = x === x
    println(s"r9 is $r9")
    val r10 = x === y
    println(s"r10 is $r10")

    import cats.instances.string._
    implicit val catEq: Eq[Cat] = Eq.instance[Cat]((c1, c2) => {
      c1.name === c2.name && c1.age === c2.age && c1.color === c2.color
    })

    val cat1 = Cat("Heathcliff", 33, "orange and black")
    val cat2 = Cat("Heathcliff", 33, "orange and black")
    val r11 = cat1 === cat2
    println(s"r11 is $r11")


    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]

    val r12 = optionCat1 === optionCat2
    println(s"r12 is $r12")
  }
  final case class Cat(name: String, age: Int, color: String)
}
