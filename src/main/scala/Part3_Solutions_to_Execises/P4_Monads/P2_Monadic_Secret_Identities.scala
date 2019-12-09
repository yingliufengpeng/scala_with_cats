package Part3_Solutions_to_Execises.P4_Monads

object P2_Monadic_Secret_Identities {

  def main(args: Array[String]): Unit = {

    import cats.Id

    def pure[A](a: A): Id[A] = a

    def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)

    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)

    val r = pure(3)
    println(s"r is $r")
  }
}
