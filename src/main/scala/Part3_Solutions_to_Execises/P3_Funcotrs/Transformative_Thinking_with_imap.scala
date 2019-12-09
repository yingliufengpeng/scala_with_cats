package Part3_Solutions_to_Execises.P3_Funcotrs

object Transformative_Thinking_with_imap {


  def main(args: Array[String]): Unit = {

    trait Codec[A] {
      def encode(value: A): String
      def decode(value: String): A

      def imap[B](dec: A => B, enc: B => A): Codec[B] = {
        val self = this
        new Codec[B] {
          override def encode(value: B): String = self.encode(enc(value))

          override def decode(value: String): B = dec(self.decode(value))
        }
      }
    }

    final case class Box[A](value: A)

    object Codec {

      def apply[A](implicit c: Codec[A]): Codec[A] = c

      implicit val stringCodec: Codec[String] =
        new Codec[String] {
          def encode(value: String): String = value
          def decode(value: String): String = value
        }

      implicit val doubCodec: Codec[Double] = new Codec[Double] {
        override def encode(value: Double): String = value.toString

        override def decode(value: String): Double = value.toDouble
      }

//      implicit val boxIntCodec: Codec[Box[Int]] =
//        stringCodec.imap(e => Box(e.toInt), _.value.toString)

      // 二次隐式搜索!!!
      implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = c.imap(Box(_), _.value)
    }

    val r = Codec.doubCodec.encode(4)
    val r2 = Codec.doubCodec.decode(r)
    println(s"r is $r, r2 is $r2")

    val r3 = Codec[Box[String]]
    println(s"r3 is ${r3.encode(Box("44"))}")

  }
}
