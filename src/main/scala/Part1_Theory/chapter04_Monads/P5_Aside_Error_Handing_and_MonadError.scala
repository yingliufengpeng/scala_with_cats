package Part1_Theory.chapter04_Monads

object P5_Aside_Error_Handing_and_MonadError {


  def main(args: Array[String]): Unit = {

    import cats.MonadError
    import cats.instances.either._  // for MonadError


    type ErrorOr[A] = Either[String, A]
    val monadError = MonadError[ErrorOr, String]

    val success = monadError.pure(32)
    println(s"success is $success")

    val failure = monadError.raiseError("Badness")
    println(s"failure is $failure")

    val r = monadError.handleError(failure) {
      case "Badness" => monadError.pure("It's Ok")
      case _ => monadError.raiseError("It's not ok")
    }
    println(s"r is $r")

    import cats.syntax.either._ // for asRight
    monadError.ensure(success)("Number too low!")(_ > 1000)
    // res3: ErrorOr[Int] = Left(Number too low!

    import cats.syntax.applicative._ // for pure
    import cats.syntax.applicativeError._ // for raiseEror etc
    import cats.syntax.monadError._       // for ensure

    val su = 42.pure[ErrorOr]
    val fa = "Badness".raiseError[ErrorOr, Int]
    val r2 = su.ensure("Number to low!")(_ > 100)
    println(s"r is $r2")

    import scala.util.Try
    import cats.instances.try_._    // for MonadError
    val exn: Throwable = new RuntimeException("It's all gone wrong")
    val r3 = exn.raiseError[Try, Int]
    println(s"r3 is $r3")
  }


}
