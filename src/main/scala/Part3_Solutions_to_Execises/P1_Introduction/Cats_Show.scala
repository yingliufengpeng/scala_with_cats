package Part3_Solutions_to_Execises.P1_Introduction

object Cats_Show {

  def main(args: Array[String]): Unit = {
    import cats.Show
    import cats.instances.int._ // for Show
    import cats.instances.string._ // for Show
    import cats.syntax.show._

    final case class Cat(name: String, age: Int, color: String)
    object Cat {
      implicit val catPrintable: Show[Cat] = cat  => s"${cat.name.show} is ${cat.age.show} year-old ${cat.color.show} cat."
    }

    val r = Cat("Garfield", 38, "ginger and black").show
    println(r)

  }
}
