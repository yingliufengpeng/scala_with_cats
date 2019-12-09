package Part3_Solutions_to_Execises.Funcotrs

object Showing_off_with_Contramap {

  def main(args: Array[String]): Unit = {

    trait Printable[A] {
      self =>

      def format(value: A): String

      def contramap[B](f: B => A): Printable[B] = new Printable[B] {
        override def format(value: B): String = self.format(f(value))
      }
    }

    object Printable {

      implicit val stringPrintalbe: Printable[String] = new Printable[String] {
        override def format(input: String): String = input
      }

      implicit val intPrintable: Printable[Int] = (input: Int) => input.toString
    }

    def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

    val r = format(33)
    val r2 = format("44")
    println(s"r is $r, r2 is $r2")
  }
}
