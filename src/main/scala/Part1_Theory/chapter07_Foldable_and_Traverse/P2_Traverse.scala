package Part1_Theory.chapter07_Foldable_and_Traverse

object P2_Traverse {

  def main(args: Array[String]): Unit = {

    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext.Implicits.global

    val hostnames = List(
      "alpha.example.com",
      "beta.example.com",
      "gamma.demo.com"
    )

    def getUptime(hostname: String): Future[Int] =
      Future(hostname.length * 60) // just for demonstration

    val allUptimes: Future[List[Int]] =
      hostnames.foldLeft(Future(List.empty[Int])) {
        (accum, host) =>
          val uptime = getUptime(host)
          for {
            accum <- accum
            uptime <- uptime
          } yield accum :+ uptime
      }
    val r = Await.result(allUptimes, 1.second)
    println(s"r is $r")

    // 前面简化的写法
    val allUptimes2: Future[List[Int]] = Future.traverse(hostnames)(getUptime)
    val r2 = Await.result(allUptimes2, 1.second)
    println(s"r2 is $r2")

    import cats.Applicative
    import cats.instances.future._ // for Applicative
    import cats.syntax.applicative._ // for pure

    val r3 = List.empty[Int].pure[Future]
    println(s"r3 is $r3")

    import cats.syntax.apply._ // for mapN
    def newCombine(accum: Future[List[Int]], host: String): Future[List[Int]] =
      (accum, getUptime(host)).mapN(_ :+ _)

    def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(List.empty[B].pure[F]) ((accum, item) => {
        (accum, func(item)).mapN(_ :+ _)
      })

    def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
      listTraverse(list)(identity)

    val totalUptime = listTraverse(hostnames)(getUptime)
    println(s"totalUptime is ${Await.result(totalUptime, 1.second)}")

    import cats.instances.vector._
    val r4 = listSequence(List(Vector(1, 2), Vector(3, 4)))
    println(s"r4 is $r4")

    val r5 = listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6)))
    println(s"r5 is $r5")

    import cats.instances.option._ // for Applicative
    def process(inputs: List[Int]): Option[List[Int]] =
      listTraverse(inputs)(n => if (n % 2  == 0 ) Some(n) else None)

    val r6 = process(List(2, 4, 6))
    val r7 = process(List(1, 2, 3))
    println(s"r6 is $r6 \n r7 is $r7")

    import cats.data.Validated
    import cats.instances.list._ // for Monoid

    type ErrorOr[A] = Validated[List[String], A]

    def process2(inputs: List[Int]): ErrorOr[List[Int]] =
      listTraverse(inputs) { n =>
        if (n % 2 == 0)
          Validated.valid(n)
        else
          Validated.invalid(List(s"$n is not even"))
      }

    val r8 = process2(List(2, 4, 6))
    val r9 = process2(List(1, 2, 3))
    println(s"r8 is $r8 \nr9 is $r9")

    import cats.Traverse
    import cats.instances.future._ // for Applicative
    import cats.instances.list._   // for Traverse

    val totalUptime3: Future[List[Int]] =
      Traverse[List].traverse(hostnames)(getUptime)

    val r10 = Await.result(totalUptime3, 1.second)
    println(s"r10 is $r10")

    val numbers = List(Future(1), Future(2), Future(3))
    val numbes2: Future[List[Int]] =
      Traverse[List].sequence(numbers)
    val r11 = Await.result(numbes2, 1.second)
    println(s"r11 is $r11")

    // 添加新的隐式语法
    import cats.syntax.traverse._   // for sequence and traverse
    val r12 = Await.result(hostnames.traverse(getUptime), 1.second)
    println(s"r12 is $r12")

    val r13 = Await.result(numbers.sequence, 1.second)
    println(s"r13 is $r13")

  }
}
