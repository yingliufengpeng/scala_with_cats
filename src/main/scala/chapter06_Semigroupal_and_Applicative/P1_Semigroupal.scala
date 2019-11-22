package chapter06_Semigroupal_and_Applicative


object P1_Semigroupal {


  def main(args: Array[String]): Unit = {

//    def liftK[F[_]](implicit F: Functor[F]): F ~> OptionT[F, ?] =
//      λ[F ~> OptionT[F, ?]](OptionT.liftF(_))

    import cats.Semigroupal
    import cats.instances.option._ // for Semigroupal
    import cats.data.OptionT
//    type M[F[_]] = OptionT[F, ?]


    val r = Semigroupal[Option].product(Some(123), Some(134))
    println(s"r is $r")

    val r2 = Semigroupal[Option].product(Some(123), None)
    println(s"r2 ir $r2")

    val r3 = Semigroupal[Option].product(None, Some("4kkk"))
    println(s"r2 ir $r3")

    import cats.instances.option._ // for Semiroupal

    val r4 = Semigroupal.tuple3(Option(1), Option(2), Option(3))
    println(s"r4 is $r4")

    val r5 = Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])
    println(s"r5 is $r5")

    val r7 = Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _ )
    println(s"r7 is $r7")

    val r8 = Semigroupal.map2(Option(2), Option.empty[Int])(_ + _)
    println(s"r8 is $r8")

  }
}
