package Part3_Solutions_to_Execises.P06_Semigroupal_and_Applicative

import sun.text.resources.cldr.mr.FormatData_mr

object P2_Form_Validation {

  def main(args: Array[String]): Unit = {

    import cats.data.Validated

    type FormData = Map[String, String]
    type FailFast[A] = Either[List[String], A]
    type FailSlow[A] = Validated[List[String], A]

    def getValue(name: String)(data: FormData): FailFast[String] =
      data.get(name).toRight(List(s"$name field not specified"))

    val getName = getValue("name") _
    println(s"getName is $getName")

    val r = getName(Map("name" -> "Dade Murphy"))
    println(s"r is $r")

    import cats.syntax.either._

    type NumFmtExn = NumberFormatException

    def parseInt(name: String)(data: String): FailFast[Int] =
      Either.catchOnly[NumFmtExn](data.toInt).leftMap(_ => List(s"$name must be an integer"))

    val r2 = parseInt("age")("3d1")
    println(s"r2 is $r2")

    def nonBlank(name: String)(data: String): FailFast[String] =
      Right(data).ensure(List(s"$name cannot be blank"))(_.nonEmpty)

    def nonNegative(name: String)(data: Int): FailFast[Int] =
      Right(data).ensure(List(s"$name must be non-negative"))(_ > 0)

    /**
     * nonBlank("name")("Dade Murphy")
     * // res36: FailFast[String] = Right(Dade Murphy)
     * nonBlank("name")("")
     * // res37: FailFast[String] = Left(List(name cannot be blank))
     * nonNegative("age")(11)
     * // res38: FailFast[Int] = Right(11)
     * nonNegative("age")(-1)
     * // res39: FailFast[Int] = Left(List(age must be non-negative))
     */

    def readName(data: FormData): FailFast[String] =
      getValue("name")(data).flatMap(nonBlank("name"))

    import cats.syntax.functor._ // for map
    import cats.syntax.flatMap._ // for flatMap

    def readAge(data: FormData): FailFast[Int] =
      getValue("age")(data).
        flatMap(nonBlank("age")).
        flatMap(parseInt("age")).
        flatMap(nonNegative("age"))

    /**
     * readName(Map("name" -> "Dade Murphy"))
     * // res41: FailFast[String] = Right(Dade Murphy)
     * readName(Map("name" -> ""))
     * // res42: FailFast[String] = Left(List(name cannot be blank))
     * readName(Map())
     * // res43: FailFast[String] = Left(List(name field not specified))
     * readAge(Map("age" -> "11"))
     * // res44: FailFast[Int] = Right(11)
     * readAge(Map("age" -> "-1"))
     * // res45: FailFast[Int] = Left(List(age must be non-negative))
     * readAge(Map())
     * // res46: FailFast[Int] = Left(List(age field not specified))
     */

    case class User(name: String, age: Int)

    import cats.syntax.apply._ // for mapN
    import cats.instances.list._

    def readUser(data: FormData): FailSlow[User] =
      (
        readName(data).toValidated,
        readAge(data).toValidated
      ).mapN(User.apply)

    /**
     * readUser(Map("name" -> "Dave", "age" -> "37"))
     * // res48: FailSlow[User] = Valid(User(Dave,37))
     * readUser(Map("age" -> "-1"))
     * // res49: FailSlow[User] = Invalid(List(name field not specified, age
     * must be non-negative))
     */

    val r3 = readUser(Map("name" -> "Dave", "age" -> "37"))
    println(s"r2 is $r3")

    val r4 = readUser(Map("age" -> "-1"))
    println(s"r4 is $r4")
  }
}
