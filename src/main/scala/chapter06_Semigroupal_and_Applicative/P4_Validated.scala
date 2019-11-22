package chapter06_Semigroupal_and_Applicative

object P4_Validated {

  def main(args: Array[String]): Unit = {


    import cats.Semigroupal
    import cats.data.Validated
    import cats.instances.list._    // for Monoid

    type AllErrorOr[A] = Validated[List[String], A]

    val r = Semigroupal[AllErrorOr].product(
      Validated.invalid(List("Error 1")),
      Validated.invalid(List("Error 2")),
    )
    println(s"r is $r")

    val v = Validated.Valid(123)
    println(s"v is $v")

    val i = Validated.Invalid(List("Badness"))
    println(s"i is $i")

    val v2 = Validated.valid[List[String], Int](123)
    println(s"v2 is $v2")
    // v: cats.data.Validated[List[String],Int] = Valid(123)
    val i2 = Validated.invalid[List[String], Int](List("Badness"))
    println(s"i2 is $i2")
    // i: cats.data.Validated[List[String],Int] = Invalid(List(Badness))

    // As a third option we can import the valid and invalid extension methods
    // from cats.syntax.validated

    import cats.syntax.validated._  // for valid and invalid

    val r2 = 123.valid[List[String]]
    println(s"r2 is $r2")

    val r3 = List("Badness").invalid[Int]
    println(s"r3 is $r3")

    // 语法糖的作用
    import cats.syntax.applicative._ // for pure
    import cats.syntax.applicativeError._ // for raiseError

    type ErrorsOr[A] = Validated[List[String], A]

    val r4 = 123.pure[ErrorsOr]
    println(s"r4 is $r4")

    val r5 = List("Badness").raiseError[ErrorsOr, Int]
    println(s"r5 is $r5")

    /**
     * Finally, there are helper methods to create instances of Validated
     * and form different sources. We can create them from Exceptions, as
     * well as instances of Try, Either, and Option.
     */
    Validated.catchOnly[NumberFormatException]("foo".toInt)
      // res7: cats.data.Validated[NumberFormatException,Int] = Invalid(java
      //.lang.NumberFormatException: For input string: "foo")

    Validated.catchNonFatal(sys.error("Badness"))
    // res8: cats.data.Validated[Throwable,Nothing] = Invalid(java.lang.
    //RuntimeException: Badness)

    Validated.fromTry(scala.util.Try("foo".toInt))
    // res9: cats.data.Validated[Throwable,Int] = Invalid(java.lang.
    //NumberFormatException: For input string: "foo")

    Validated.fromEither[String, Int](Left("Badness"))
    // res10: cats.data.Validated[String,Int] = Invalid(Badness)

    Validated.fromOption[String, Int](None, "Badness")
    // res11: cats.data.Validated[String,Int] = Invalid(Badness)

    type AllErrorOr2[A] = Validated[String, A]
    import cats.instances.string._

    val r6 = Semigroupal[AllErrorOr2]
    println(s"r6 is $r6")

    import cats.syntax.apply._ // for tupled

    val r7 = (
      "Error 1".invalid[Int],
      "Error 2".invalid[Int],
    ).tupled
    println(s"r7 is $r7")

    import cats.instances.vector._  // for Semigroupal
    val r8 = (
      Vector(404).invalid[Int],
      Vector(500).invalid[Int],
    ).tupled
    println(s"r8 is $r8")

    import cats.data.NonEmptyVector
    val r9 = (
      NonEmptyVector.of("Error 1").invalid[Int],
      NonEmptyVector.of("Error 2").invalid[Int],
    ).tupled
    println(s"r9 is $r9")

    val r10 = 123.valid.map(_ * 10)
    println(s"r10 is $r10")

    val r11 = "?".invalid.leftMap(_.toString)
    println(s"r11 is $r11")

    val r12 = 123.valid[String].bimap(_ + "!", _ * 100)
    println(s"r12 is $r12")

    val r13 = "?".invalid[Int].bimap(_ + "!", _ * 100)
    println(s"r13 is $r13")

    /**
     * We can’t flatMap because Validated isn’t a monad. However, Cats does
     * provide a stand-in for flatMap called andThen . The type signature of
     * andThen is identical to that of flatMap , but it has a different name because
     * it is not a lawful implementation with respect to the monad laws:
     */

    val r14 = 32.valid.andThen(a => {
      10.valid.map(b => a + b)
    })
    println(s"r14 is $r14")

    /**
     * If we want to do more than just flatMap, we can convert back and forth
     * between Validated and Either using the toEither and toValidated methods.
     * Note that toValidated comes from [cats.syntax.either]
     */
    import cats.syntax.either._ // for toValidated
    // import cats.syntax.either._
    "Badness".invalid[Int]
    // res22: cats.data.Validated[String,Int] = Invalid(Badness)
    "Badness".invalid[Int].toEither
    // res23: Either[String,Int] = Left(Badness)
    "Badness".invalid[Int].toEither.toValidated
    // res24: cats.data.Validated[String,Int] = Invalid(Badness


    val r15 = 123.valid[String].ensure("Negative!")(_ > 444)
    println(s"r15 is $r15")

    /**
     * Finally, we can call getOrElse or fold to extract values from the
     * Valid and Invalid cases;
     */
    val r16 = "fail".invalid[Int].getOrElse(0)
    println(s"r16 is $r16")

    val r17 = "fail".invalid[Int].fold(_ + "!!!", _.toString)
    println(s"r17 is $r17")

  }
}
