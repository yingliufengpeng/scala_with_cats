package Part3_Solutions_to_Execises.P1_Solutions_for_Introduction

object Equality_Liberty_Felinity {

  def main(args: Array[String]): Unit = {

    import cats.Eq // trait
    import cats.syntax.eq._ // for ===
    import cats.instances.int._ // for Eq
    import cats.instances.string._ // for Eq

    final case class Cat(name: String, age: Int, color: String)

    object Cat {
      implicit val catEqual: Eq[Cat] = (cat1, cat2) =>
        cat1.name === cat2.name && cat1.age === cat2.age && cat1.color === cat2.color
    }


    val cat1 = Cat("wang", 39, "black")
    val cat2 = Cat("wang", 39, "black")

    val r = cat1 === cat2
    println(s"r is $r")

    val r2 = cat1 =!= cat2
    println(s"r2 is $r2")

    import cats.instances.option._ // for Eq
    val optionCat1 = Option(cat1)
    val optionCat2 = Option.empty[Cat]

    val r3 = optionCat1 === optionCat2
    val r4 = optionCat1 =!= optionCat2

    println(s"r3 is $r3, r4 is $r4")



  }

}
