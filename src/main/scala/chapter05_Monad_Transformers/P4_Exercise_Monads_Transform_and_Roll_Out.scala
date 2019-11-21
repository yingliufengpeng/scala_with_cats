package chapter05_Monad_Transformers

import cats.data.{EitherT, OptionT}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import cats.instances.future._
import cats.syntax.OptionIdOps
//import cats.instances.either._
//import cats.instances.option._
import cats.syntax.applicative._
import scala.concurrent.ExecutionContext.Implicits.global


object P4_Exercise_Monads_Transform_and_Roll_Out {


  def main(args: Array[String]): Unit = {

    val powerLevels = Map(
      "Jazz" -> 6,
      "Bumblebee" -> 8,
      "Hot Rod" -> 10
    )
//    type Response[A] = Future[Either[String, A]]  // 这种把多个monad组合在一起使用的确体验不凡

    type FutureEither[A] = EitherT[Future, String, A]  // FutureEither[A] = Future[Either[String, A]]
    type Response[A] = OptionT[FutureEither, A]       // FutureEither[Option[A]]  ==> Future[Either[String, Option[A]]]
                                                      //
    // defined type alias Response
    def getPowerLevel(autobot: String): Response[Int] = {
//      val res = powerLevels.get(autobot)
//      res match {
//        case Some(r) => r.pure[Response]
//        case None =>
////          val m = "kkk".pure[FutureEither].map(e => e.pure[Response])
//          val fu = Future[Either[String, Option[Int]]](Left("KKK"))
//          fu.map(e => e.right.get.get.pure[Response])
//      }
      ???
    }



    def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
      ???


    def tacticalReport(ally1: String, ally2: String): String = {
      val r = for {
        a <- getPowerLevel(ally1)
        b <- getPowerLevel(ally2)
        c <- canSpecialMove(ally1, ally2)

      } yield a + b

      val res = Await.result(r.value.value, 1.second)

//      res match {
//        case Left(msg) => msg
//        case Right(Some(ok)) => ok.toString
//      }

      ???

    }



    val m = 3.pure[Response]
    println(s"m is $m")
    val futureEither0r = for {
      a <- 10.pure[Response]
      b <- 32.pure[Response]
    } yield a + b



//    tacticalReport("Jazz", "Bumblebee")
//    // res28: String = Jazz and Bumblebee need a recharge.
//    tacticalReport("Bumblebee", "Hot Rod")
//    // res29: String = Bumblebee and Hot Rod are ready to roll out!
//    tacticalReport("Jazz", "Ironhide")
//    // res30: String = Comms error: Ironhide unreachable
  }
}
