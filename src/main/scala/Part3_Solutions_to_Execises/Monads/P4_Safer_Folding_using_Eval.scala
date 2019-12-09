package Part3_Solutions_to_Execises.Monads

object P4_Safer_Folding_using_Eval {

  def main(args: Array[String]): Unit = {
    import cats.Eval

    @scala.annotation.tailrec
    def foldRightEval[A, B](as: Traversable[A])(acc: Eval[B])(f:(A, Eval[B]) => Eval[B]): Eval[B] =
      as match {
        case Nil => acc
        case h:: tail => foldRightEval(tail)(f(h, acc))(f)
      }

    def foldRight[A, B](as: Traversable[A])(acc: B)(f: (A, B) => B): B =
      foldRightEval(as)(Eval.later(acc))((a, acc) => acc.map(f(a, _))).value

    val r = foldRight((1 to 1000000).toList)(BigInt(0))(BigInt(_) + _)
    println(s"r is ${r}")

  }

}
