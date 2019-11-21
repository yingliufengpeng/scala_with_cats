package chapter05_Monad_Transformers

object P3_Monad_Transformers_in_Cats {

  def main(args: Array[String]): Unit = {
    // Alias Either to a type constructor with one parameter

    // Build our final monad stack using OptionT
    import cats.data.OptionT
    type ErrorOr[A] = Either[String, A]
    type ErrorOrOption[A] = OptionT[ErrorOr, A]

    import cats.syntax.applicative._
    import cats.instances.either._

    val a = 10.pure[ErrorOrOption]
    val b = 23.pure[ErrorOrOption]

    val d = OptionT(Left[String, Option[Int]]("33"))
    println(s"d is $d")

    println(s"a is $a b is $b")
    a.flatMap(x => b.map(y => x + y))   // 看来是当前的编译器的插件并没有找到隐式值!!!
    val cc = for {
      x <- a
      y <- b
    } yield x + y
    println(s"cc is ${cc.value}")

    import scala.concurrent.Future
    import cats.data.{EitherT, OptionT}

    type FutureEither[A] = EitherT[Future, String, A]
    type FutureEitherOption[A] = OptionT[FutureEither, A]

    import cats.instances.future._  // for Monad
    import scala.concurrent.Await
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val m = 30.pure[FutureEitherOption]
    println(s"m is $m")

    val futureEither0r = for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield a + b
//    Thread.sleep(3000)
    println(s"futureEither0r is ${futureEither0r.value}")

    val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
    println(s"errorStack1 is $errorStack1")

    val errorStack2 = 32.pure[ErrorOrOption]
    println(s"errorStack2 is $errorStack2")

    println(s"errorStack1.value ${errorStack1.value} ")

    println(s"errorStack2.value.map(_.getOrElse(-1)) ${errorStack2.value.map(_.getOrElse(-1))} ")

    println(s"futureEither0r.value.value is ${Await.result(futureEither0r.value.value, 1.second)}")

    /**
     *  Usage Patterns
     *  Widespread use of monad transformers is sometimes difficult because they fuse monads together
     *  in predefined ways. Without careful thought, we can end up having to unpack and repack monads
     *  in different configurations to operate on them in different contexts.
     *
     *  We can cope with this in multiple ways. One approach involves creating a single "super stack"
     *  and sticking to it throughout our code base. This works if the code is simple and largely uniform
     *  in nature. For example, in a web application, we could decide that all request handers are
     *  asynchronous and all can fail with the same set of HTTP error codes. We could design a custom
     *  ADT representing the errors and use a fusion Future and Either everywhere in our code
     */

    sealed abstract class HttpError
    final case class NotFound(item: String) extends HttpError
    final case class BadRequest(msg: String) extends HttpError
    // etc...
    type FutureEither2[A] = EitherT[Future, HttpError, A]

    /**
     *  The "super stack" approach starts to fail in larger, more heterogeneous code bases where different
     *  stack make sence in different contexts. Another design pattern that makes more sense in these context
     *  monads transformers as local "glue code". We expose untransformed stacks at module boundaries, transformers
     *  them to operate on them locally, and untransform them before passing them on. This allow each module
     *  of code to make its own decisions about which transformers to use.
     */

    import cats.data.Writer

    type Logged[A] = Writer[List[String], A]
    // Methods generally return untransformed stacks
    def parseNumber(str: String): Logged[Option[Int]] =
      util.Try(str.toInt).toOption match {
        case Some(num) => Writer(List(s"Read $str"), Some(num))
        case None =>      Writer(List(s"Failed on $str"), None)
      }

    // Consumers use monad transformers locally to simplify composition
    def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {
      import cats.data.OptionT
//      import cats.instances.option._
      import cats.instances.list._
      val result = for {
        a <- OptionT(parseNumber(a))
        b <- OptionT(parseNumber(b))
        c <- OptionT(parseNumber(c))
      } yield a + b + c
      result.value
    }

     //This approach doesn't force OptionT on other users' code
    val result1 = addAll("1", "2", "3")
    println(s"result1 is $result1")

    val result2 = addAll("1", "a", "3")
    println(s"result2 is $result2")


  }
}
