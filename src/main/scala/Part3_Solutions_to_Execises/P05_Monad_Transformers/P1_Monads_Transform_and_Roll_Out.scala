package Part3_Solutions_to_Execises.P05_Monad_Transformers

import java.util.ResourceBundle

object P1_Monads_Transform_and_Roll_Out {

  def main(args: Array[String]): Unit = {

    import cats.data.EitherT
    import scala.concurrent.Future
    import cats.instances.future._ // for Monad
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Await
    import scala.concurrent.duration._

    type Response[A] = EitherT[Future, String, A]

    val powerLevels = Map(
      "Jazz" -> 6,
      "Bumblebee" -> 8,
      "Hot Rod" -> 10
    )

    def getPowerLevel(ally: String): Response[Int] = powerLevels.get(ally) match {
      case Some(v) => EitherT.right(Future(v))
      case None => EitherT.left(Future(s"$ally unreachable"))
    }

    def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = for {
      power1 <- getPowerLevel(ally1)
      power2 <- getPowerLevel(ally2)
    } yield (power1 + power2) > 15

    def taticalRepot(ally1: String, ally2: String): String = {
      val stack = canSpecialMove(ally1, ally2).value
      Await.result(stack, 1.second) match {
        case Left(msg) =>
          s"Comms error: $msg"
        case Right(true) =>
          s"$ally1 and $ally2 are ready to roll out!"
        case Right(false) =>
          s"$ally1 and $ally2 need a recharge"
      }
    }

    val r = taticalRepot("Jazz", "Hot Rod")
    println(s"r is $r")

  }
}
