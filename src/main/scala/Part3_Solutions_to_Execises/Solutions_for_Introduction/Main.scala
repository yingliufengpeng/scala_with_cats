package Part3_Solutions_to_Execises.Solutions_for_Introduction

object Main {


  def main(args: Array[String]): Unit = {

    trait Printable[A] {
      def format(value: A): String
    }

    object PrintableInstances {

      // 为了演示这种写法是可以实现这样的逻辑!!!
      implicit val stringPrintalbe: Printable[String] = new Printable[String] {
        override def format(input: String): String = input
      }

      implicit val intPrintable: Printable[Int] = (input: Int) => input.toString
    }

    object Printable {
      def format[A](input: A)(implicit p: Printable[A]): String =  p.format(input)

      def print[A](input: A)(implicit p: Printable[A]): Unit = println(format(input))
    }

    final case class Cat(name: String, age: Int, color: String)

    object Cat {
      implicit val catPrintable: Printable[Cat] = cat  => s"${cat.name} is ${cat.age} year-old ${cat.color} cat."
    }

    object PrintableSyntax {
      implicit class PrintableOps[A](value: A)(implicit p: Printable[A]) {
        def format: String = Printable.format(value)
        def print: Unit = Printable.print(value)
      }
    }

    val cat = Cat("wang", 30, "black")
    Printable.print(cat)

    import PrintableSyntax._
    cat.print

  }
}
