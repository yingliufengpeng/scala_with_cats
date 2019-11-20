package chapter03_Functors

object P7_Contravariant_and_Invariant_in_Cats {

  def main(args: Array[String]): Unit = {
    import cats.Contravariant
    import cats.Show
    import cats.instances.string._

    val showString = Show[String]
    val showSymbol = Contravariant[Show]
      .contramap(showString)((sym: Symbol) => s"'${sym.name}")
    val r = showString.show("我的第一个测试")
    val r2 = showSymbol.show('你是谁)
    println(s"r is $r, r2 is $r2")

    import cats.syntax.contravariant._ // for contramap
    val r3 = showString.contramap[Symbol](_.name).show('映柳枫鹏)
    println(s"r3 is $r3")

    import cats.Monoid
    import cats.syntax.invariant._
    import cats.syntax.semigroup._
    implicit val symbolMonoid: Monoid[Symbol] = Monoid[String].imap(Symbol.apply)(_.name)
    val r4 = Monoid[Symbol].empty
    val r5 = 'a |+| 'few |+| 'words
    println(s"r4 is $r4, r5 is $r5")

  }

}
