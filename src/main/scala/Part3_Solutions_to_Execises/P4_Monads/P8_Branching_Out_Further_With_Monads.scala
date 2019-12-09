package Part3_Solutions_to_Execises.P4_Monads

import jdk.nashorn.internal.ir.BreakableNode

import scala.annotation.tailrec

object P8_Branching_Out_Further_With_Monads {

  def main(args: Array[String]): Unit = {
    import cats.Monad

    sealed trait Tree[+A]
    final case class Branch[A](left: Tree[A], right: Tree[A])
      extends Tree[A]
    final case class Leaf[A](value: A) extends Tree[A]

    object Tree {
      def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
        Branch(left, right)

      def leaf[A](value: A): Tree[A] =
        Leaf(value)
    }

    implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
      override def pure[A](x: A): Tree[A] = Tree.leaf(x)

      override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match {
        case Leaf(v) => f(v)
        case Branch(le, ri) => Branch(flatMap(le)(f), flatMap(ri)(f))
      }

      def tailRecM2[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
        flatMap(f(a)){
          case Left(v) => tailRecM2(v)(f)
          case Right(v) => Tree.leaf(v)
        }


     // 这块的代码并不能很好的理解,可能需要一些其他的知识!!!
      def tailRecM[A, B](v: A)
                        (func: A => Tree[Either[A, B]]): Tree[B] = {
        @tailrec
        def loop(
                  open: List[Tree[Either[A, B]]],
                  closed: List[Option[Tree[B]]]): List[Tree[B]] =
          open match {
            case Branch(l, r) :: next =>
              loop(l :: r :: next, None :: closed)
            case Leaf(Left(value)) :: next =>
              loop(func(value) :: next, closed)
            case Leaf(Right(value)) :: next =>
              loop(next, Some(pure(value)) :: closed)
            case Nil =>
              closed.foldLeft(Nil: List[Tree[B]]) { (acc, maybeTree) =>
                maybeTree.map(_ :: acc).getOrElse {
                  val left :: right :: tail = acc
                  Tree.branch(left, right) :: tail
                }
              }
          }


        loop(List(func(v)), Nil).head
      }
    }

    import cats.syntax.functor._ // for map
    import cats.syntax.flatMap._ // for flatMap

    val tree = Tree.branch(Tree.leaf(100), Tree.leaf(200))

    val tree2 = tree.flatMap(x => Tree.branch(Tree.leaf(x - 1), Tree.leaf(x + 1)))

    println(s"tree is $tree")
    println(s"tree2 is $tree2")

    val r = for {
      a <- Tree.branch(Tree.leaf(100), Tree.leaf(200))
      b <- Tree.branch(Tree.leaf(a - 10), Tree.leaf(a + 10))
      c <- Tree.branch(Tree.leaf(b - 1), Tree.leaf(b + 1))
    } yield c

    println(s"r is $r")



  }

}
