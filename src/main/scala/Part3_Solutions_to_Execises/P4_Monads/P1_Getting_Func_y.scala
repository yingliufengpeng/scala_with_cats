package Part3_Solutions_to_Execises.P4_Monads

object P1_Getting_Func_y {

  def main(args: Array[String]): Unit = {

    trait Monad[F[_]] {
      def pure[A](value: A): F[A]

      def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

      def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => pure(f(a)))
    }
  }
}
