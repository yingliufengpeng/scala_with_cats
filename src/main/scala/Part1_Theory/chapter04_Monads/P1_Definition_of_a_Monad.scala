package Part1_Theory.chapter04_Monads

object P1_Definition_of_a_Monad {

  trait Monad[F[_]] {
    def pure[A](value: A): F[A]

    def flatMap[A, B](value: F[A])(f: A => F[B]): F[B]

    def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)(a => pure(func(a)))


  }

  /**Monad Laws
    pure and flatMap must obey a set of laws that allow us to sequence
    operations freely without unintended glitches and side-effects:
    Lety identity: calling pure and transforming the result with func is the
    same as calling func :
          pure(a).flatMap(func) == func(a)
    Right identity: passing pure to flatMap is the same as doing nothing:
          m.flatMap(pure) == m
    Associativity: flatMapping over two functions f and g is the same as
          flatMapping over f and then flatMapping over g :
    m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
   *
   *
   */

  def main(args: Array[String]): Unit = {


  }
}
