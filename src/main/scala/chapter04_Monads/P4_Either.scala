package chapter04_Monads

object P4_Either {

  def main(args: Array[String]): Unit = {

    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(20)
    val r = for {
      a <- either1
      b <- either2
    } yield a + b

    println(s"r is $r")

    import cats.syntax.either._ // for asRight
    val a = 3.asRight[String]
    println(s"a is $a")

    val b = 4.asRight[String]
    println(s"b is $b")

    val c = for {
      x <- a
      y <- b
    } yield x * x + y * y

    println(s"c is $c")

    //一下则是编译器通不过的代码
    //    def countPositive(nums: List[Int]) =
    //      nums.foldLeft(Right(0)){ (accumulator, num) => {
    //        if (num > 0)
    //          accumulator.map(_ + 1)
    //        else {
    //          Left("Negative. Stoping")
    //        }
    //      }}

    def countPositive(nums: List[Int]): Either[String, Int] =
      nums.foldLeft(0.asRight[String]) { (accumulator, num) => {
        if (num > 0)
          accumulator.map(_ + 1)
        else {
          Left("Negative. Stoping")
        }
      }
      }

    val r2 = countPositive((1 to 3).toList)
    println(s"r2 is $r2")

    val r3 = countPositive(List(1, -2, 3))
    println(s"r3 is $r3")

    val r4 = Either.fromTry(scala.util.Try("foo".toInt))
    println(s"r4 is $r4")

    val r5 = Either.fromOption[String, Int](None, "kdkdk")
    println(s"r5 is $r5")

    val r6 = "Error".asLeft[Int].getOrElse(0)
    println(s"r6 is $r6")

    val r7 = "Error".asLeft[Int].orElse(2.asRight[String])
    println(s"r7 is $r7")

    val r8 = -1.asRight[String].ensure("Must be non-negative")(_ > 0)
    println(s"r8 is $r8")

    val r9 = "error".asLeft[Int].recover {
      case str: String => -1
    }
    println(s"r9 is $r9")

    val r10 = "error".asLeft[Int].recoverWith {
      case str: String => Right(-1)
    }
    println(s"r10 is $r10")

    val r11 = "foo".asLeft[Int].leftMap(_.reverse)
    println(s"r11 is $r11")

    val r12 = 6.asRight[String].bimap(_.reverse, _ * 7)
    println(s"r12 is $r12")

    val r13 = "bar".asLeft[Int].bimap(_.reverse, _ * 7)
    println(s"r13 is $r13")

    val r14 = 123.asRight[String].swap
    println(s"r14 is $r14")

    sealed trait LoginError extends Product with Serializable
    final case class UserNotFound(username: String)
      extends LoginError
    final case class PasswordIncorrect(username: String) extends LoginError
    case object UnexpectedError extends LoginError
    case class User(username: String, password: String)
    type LoginResult = Either[LoginError, User]

    // Choose error-handling behaviour based on type:
    def handleError(error: LoginError): Unit =
      error match {
        case UserNotFound(u) =>
          println(s"User not found: $u")
        case PasswordIncorrect(u) =>
          println(s"Password incorrect: $u")
        case UnexpectedError =>
          println(s"Unexpected error")
      }

    val result1: LoginResult = User("dave", "passw0rd").asRight
    // result1: LoginResult = Right(User(dave,passw0rd))
    val result2: LoginResult = UserNotFound("dave").asLeft
    // result2: LoginResult = Left(UserNotFound(dave))
    result1.fold(handleError, println)
    // User(dave,passw0rd)
    result2.fold(handleError, println)


  }
}
