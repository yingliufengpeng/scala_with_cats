package Part1_Theory.chapter04_Monads

object P8_The_Reader_Monad {
  def main(args: Array[String]): Unit = {
    import cats.data.Reader
    case class Cat(name: String, favoriteFood: String)

    val catName: Reader[Cat, String] = Reader(cat => cat.name)
    val r = catName.run(Cat("Garfield", "lasgne"))
    println(s"r is $r")

    val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello, $name")
    val r2 = greetKitty.run(Cat("Heathcliff", "junk food"))
    println(s"r2 is $r2")

    val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

    val greetAndFeed: Reader[Cat, String] =
      for {
        greet <- greetKitty
        feed <- feedKitty
      } yield s"$greet. $feed"

    val r3 = greetAndFeed(Cat("Garfiled", "lasagne"))
    println(s"r3 is $r3")

    case class Db(userNames: Map[Int, String], password: Map[String, String])
    type DbReader[A] = Reader[Db, A]

    def findUsername(userId: Int): DbReader[Option[String]] = Reader(db => db.userNames.get(userId))

    def checkPassword(
                       username: String,
                       password: String): DbReader[Boolean] = Reader {
      db =>
        val r = for {
          (_, uname) <- db.userNames if uname == username
          _ <- db.password.get(uname)
        } yield true
        r.nonEmpty
    }

    def checkLogin(
                    userId: Int,
                    password: String): DbReader[Boolean] = Reader {
      db =>
        val r = for {
          some_uname <- findUsername(userId)
          v <- checkPassword(some_uname.getOrElse(""), password)
        } yield v

//        val r = for {
//          uname <- db.userNames.get(userId)
//          _ <- db.password.get(uname)
//        } yield true
        r.run(db)
    }

    val users = Map(
      1 -> "dade",
      2 -> "kate",
      3 -> "margo"
    )

    val passwords = Map(
      "dade" -> "zerocool",
      "kate" -> "acidburn",
      "margo" -> "secret"
    )
    val db = Db(users, passwords)
    val r4 = checkLogin(1, "zerocool").run(db)
    println(s"r4 is $r4")
    // res10: cats.Id[Boolean] = true
    val r5 = checkLogin(4, "davinci").run(db)
    println(s"r5 is $r5")
    // res11: cats.Id[Boolean] = false


  }
}
