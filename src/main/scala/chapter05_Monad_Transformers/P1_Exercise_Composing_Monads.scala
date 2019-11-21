package chapter05_Monad_Transformers

object P1_Exercise_Composing_Monads {

  def main(args: Array[String]): Unit = {

    case class User(name: String)

    def lookupUser(l: Long): Option[User] = ???

    def lookupUserName(id: Long): Either[Error, Option[String]] = ???
//      val m = for {
//        optUser <- lookupUser(id)
//      } yield {
////        for { user <- optUser } yield user.name
//        optUser.name
//      }

    import cats.Monad
    import cats.syntax.applicative._ // for pure
    import cats.syntax.flatMap._ // for flatMap
    import scala.language.higherKinds

    //    def compose[M1[_]: Monad, M2[_]: Monad] = {
    //
    //      type Composed[A] = M1[M2[A]]
    //
    //      new Monad[Composed] {
    //        override def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] = {
    //
    //          // Problem! how do we write flatMap?
    //          ???
    //        }
    //
    //        override def tailRecM[A, B](a: A)(f: A => Composed[Either[A, B]]): Composed[B] = ???
    //
    //        override def pure[A](x: A): Composed[A] = x.pure[M2].pure[M1]
    //      }
    //    }

    def compose[M1[_] : Monad, M2[_] : Monad] = {

      type Composed[A] = M1[M2[A]]

      new Monad[Composed] {
        override def flatMap[A, B](fa: Composed[A])(f: A => Composed[B]): Composed[B] = {

          //          fa.flatMap(_.fold[Composed[B]](None.pure[M1])(f))
          ???
        }


        override def tailRecM[A, B](a: A)(f: A => Composed[Either[A, B]]): Composed[B] = ???

        override def pure[A](x: A): Composed[A] = x.pure[M2].pure[M1]
      }

    }
  }
}
