package chapter03_Functors

object P6_Contravariant_and_Invarant_Functors {

  trait Printable[A] { outer =>
    def format(value: A): String

    def map[B](func: A => B): Printable[B] = new Printable[B] {
      override def format(value: B): String = ???  // map应该是作用域相关容器的逻辑?
    }

    // 逆变的思路很是有趣
    def contramap[B](func: B => A): Printable[B] = new Printable[B] {
      override def format(value: B): String = outer.format(func(value))
    }

  }

  object Printable {

    implicit val stringPrintable: Printable[String] =
      new Printable[String] {
        def format(value: String): String =
          "\"" + value + "\""
      }

    implicit val booleanPrintable: Printable[Boolean] =
      new Printable[Boolean] {
        def format(value: Boolean): String =
          if(value) "yes" else "no"
      }

    implicit def box[A]: Printable[Box[A]] = new Printable[Box[A]] {
      override def format(value: Box[A]): String = s"Box is $value"
    }


  }

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  final case class Box[A](value: A)

  trait Codec[A] { outer =>
    def encode(value: A): String
    def decode(value: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
      // 很显然,解码之前那肯定是最先个那个做编码,然后是接着编码
      override def encode(value: B): String = outer.encode(enc(value))
      // 同样的道理解码肯定是倒着进行解码的操作
      override def decode(value: String): B = dec(outer.decode(value))
    }
  }

  object Codec {
    implicit val stringCodec: Codec[String] =
      new Codec[String] {
        def encode(value: String): String = value
        def decode(value: String): String = value
      }

    implicit val intCodec: Codec[Int] =
      stringCodec.imap(_.toInt, _.toString)   // 第一个dec 第二个则是enc这两种思路的逻辑的展望的思
    implicit val douCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)
    implicit val booleanCodec: Codec[Boolean] =
      stringCodec.imap(_.toBoolean, _.toString)

    implicit val boxintCodec: Codec[Box[Int]] = intCodec.imap(Box(_), _.value)

  }

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

  def main(args: Array[String]): Unit = {

//    val r = format("hello")
//    println(s"r is $r")
//
//    val r2 = format(true)
//    println(s"r2 is $r2")
//
//    val r3 = format(Box("hello world"))
//    println(s"r3 is $r3")
//
//    val r4 = format(Box(true))
//    println(s"r4 is $r4")

//    val r5 = encode(134)
//    println(s"r5 is $r5")
//    val r6 = decode[Int](r5)
//    println(s"r6 is $r6")
//    val r7 = decode[Double](r5)
//    println(s"r7 is $r7")

    val r8 = encode(Box(22))
    println(s"r8 is $r8")
    val r9 = decode[Box[Int]](r8)
    println(s"r9 is $r9")


  }
}
