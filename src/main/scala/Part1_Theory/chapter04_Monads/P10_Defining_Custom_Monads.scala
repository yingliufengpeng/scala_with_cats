package Part1_Theory.chapter04_Monads

object P10_Defining_Custom_Monads {


  def main(args: Array[String]): Unit = {
    import cats.Monad
    import scala.annotation.tailrec // for 尾递归注释

    val optionMonad = new Monad[Option] {
      override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa flatMap f

      override def pure[A](x: A): Option[A] = Some(x)

      @tailrec
      override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] = f(a) match {
        case None => None
        case Some(Left(a1)) => tailRecM(a1)(f)
        case Some(Right(b)) => Some(b)
      }
    }

    sealed trait Tree[+A]

    final case class Branch[A](left: Tree[A], right: Tree[A])
      extends Tree[A]

    final case class Leaf[A](value: A) extends Tree[A]

    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
      Branch(left, right)

    def leaf[A](value: A): Tree[A] =
      Leaf(value)

    val treeMonad = new Monad[Tree] {
      override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = ???

      override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = ???

      override def pure[A](x: A): Tree[A] = leaf(x)
    }


  }
}
