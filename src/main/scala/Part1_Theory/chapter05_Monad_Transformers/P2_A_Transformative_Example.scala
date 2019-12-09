package Part1_Theory.chapter05_Monad_Transformers

object P2_A_Transformative_Example {


  def main(args: Array[String]): Unit = {


    import cats.data.OptionT

    type ListOption[A] = OptionT[List, A] // 类型的引用用的真的是不少的情况

    import cats.instances.list._  // for Monad
    import cats.syntax.applicative._  // for pure


    val result1: ListOption[Int] = OptionT(List(Option(50)))
//    val result12: ListOption[Int] = OptionT(List(Option(10)))

    val result2: ListOption[Int] = 32.pure[ListOption]
    // result2: ListOption[Int] = OptionT(List(Some(32)))

    val m = for {
      x <- result1
      y <- result2
    } yield  x + y

    println(s"m is ${m}")

    /**
     * Complexity of Imports
     * The imports in the code samples above hint at how everything bolts
     * together.
     * We import cats.syntax.applicative to get the pure syntax. pure
     * requires an implicit parameter of type Applicative[ListOption] .
     * We haven’t met Applicatives yet, but all Monads are also Applica-
     * tives so we can ignore that difference for now.
     * In order to generate our Applicative[ListOption] we need in-
     * stances of Applicative for List and OptionT . OptionT is a Cats data
     * type so its instance is provided by its companion object. The instance
     * for List comes from cats.instances.list .
     * Notice we’re not importing cats.syntax.functor or
     * cats.syntax.flatMap . This is because OptionT is a concrete
     * data type with its own explicit map and flatMap methods. It wouldn’t
     * cause problems if we imported the syntax—the compiler would ignore
     * it in favour of the explicit methods.
     * Remember that we’re subjecting ourselves to these shenanigans be-
     * cause we’re stubbornly refusing to use the universal Cats import,
     * cats.implicits . If we did use that import, all of the instances and
     * syntax we needed would be in scope and everything would just work.
     */


  }

}
