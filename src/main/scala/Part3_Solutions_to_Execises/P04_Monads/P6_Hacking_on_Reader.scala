package Part3_Solutions_to_Execises.P04_Monads

import cats.data.Reader
import cats.syntax.applicative._ // for pure

object P6_Hacking_on_Reader {

  def main(args: Array[String]): Unit = {

    case class Db(userNames: Map[Int, String], passwords: Map[String, String])
    type DbReader[A] = Reader[Db, A]

    def findUsername(userId: Int):DbReader[Option[String]] =
      Reader (db => db.userNames.get(userId))

    def checkPassword(
                     username: String,
                     password: String,
                     ): DbReader[Boolean] =
      Reader(db => db.passwords.get(username).contains(password))

    def checkLogin(userId: Int, password: String): DbReader[Boolean] =
      for {
        usernameOption <- findUsername(userId)
        passwordOk <- usernameOption.map(checkPassword(_, password)).getOrElse(false.pure[DbReader])
      } yield passwordOk

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
