package Part3_Solutions_to_Execises.Funcotrs



object Branching_out_with_Functions {

  def main(args: Array[String]): Unit = {

    import cats.Functor
    import cats.syntax.functor._ // for map

    sealed trait Tree[+A]
    final case class Branch[A](left: Tree[A], right: Tree[A])
      extends Tree[A]
    final case class Leaf[A](value: A) extends Tree[A]

    object Tree {
      def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
      def leaf[A](v: A): Tree[A] = Leaf(v)
    }

    implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
      override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
        case Branch(le, ri) =>
          Branch(map(le)(f), map(ri)(f))
        case Leaf(n) => Leaf(f(n))
      }
    }


    val r = Tree.leaf(100).map(_ * 2)
    println(s"r is $r")

    val r2 = Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)
    println(s"r2 is $r2")


  }
}
