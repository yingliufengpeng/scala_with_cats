package Part1_Theory.chapter06_Semigroupal_and_Applicative

object P0_Semigroupal_and_Applicative {

  def main(args: Array[String]): Unit = {

    import cats.syntax.either._  // for catchOnly

    def parseInt(str: String): Either[String, Int] =
      Either.catchOnly[NumberFormatException](str.toInt)
      .leftMap(_ => s"Cloudn't read $str")

    val m = for {
      a <- parseInt("a")
      b <- parseInt("b")
      c <- parseInt("c")
    } yield a + b + c

    println(s"m is $m")
  }
}
