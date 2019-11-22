package chapter06_Semigroupal_and_Applicative

object P2_Apply_Synatx {


  def main(args: Array[String]): Unit = {
//    import cats.instances.option._    // for Semigroupal
//    import cats.syntax.apply._        // for tupled and mapN
//
//    val r = (Option(123), Option("abc")).tupled
//    println(s"r is $r")
//
//    val r2 = (Option(33), Option.empty[String]).tupled
//    println(s"r2 is $r2")
//
//    val r3 = (Option(123), Option("abc"), Option(true)).tupled
//    println(s"r3 is $r3")
//
//    case class Cat(name: String, born: Int, color: String)
//
//    val r4 = (
//      Option("映柳楓鵬"),
//      Option(1991),
//      Option("黑色"),
//    ).mapN(Cat)
//    println(s"r4 is $r4")
//
//    /**
//     * It’s nice to see that this syntax is type checked. If we supply a function that
//     * accepts the wrong number or types of parameters, we get a compile error:
//     */
//    val add: (Int, Int) => Int = (a, b) => a + b
//    val r5 = (Option(1), Option(2)).mapN(add)
//    println(s"r5 is $r5")


    import cats.Monoid
    import cats.instances.int._           // for Monoid
    import cats.instances.invariant._     // for Semigroupal
    import cats.instances.list._          // for Monoid
    import cats.instances.string._        // for Monoid
    import cats.syntax.apply._            // for imapN


    case class Cat2(
                    name: String,
                    yearOfBirth: Int,
                    favoriteFoods: List[String]
                  )

    val tupleToCat: (String, Int, List[String]) => Cat2 =  Cat2.apply

    val catToTuple: Cat2 => (String, Int, List[String]) =
      cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

    implicit val catMonoid: Monoid[Cat2] = (
      Monoid[String],
      Monoid[Int],
      Monoid[List[String]],
    ).imapN(tupleToCat)(catToTuple)

    println(s"catMonoid is $catMonoid")

    import cats.syntax.semigroup._    // for |+|
    val garfield = Cat2("Garfield", 1978, List("Lasagne"))
    val heathcliff = Cat2("Heathcliff", 1988, List("Junk Food"))
    val r = garfield |+| heathcliff
    println(s"r is $r")


  }
}
