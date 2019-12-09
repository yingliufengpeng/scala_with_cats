package Part1_Theory.chapter01_Introduction

object P1_Anatomy_of_a_Type_Class {

  // Define a very simple JSON AST   # 代表的Json格式的相关信息
  sealed trait Json

  final case class JsObject(get: Map[String, Json]) extends Json

  final case class JsString(get: String) extends Json

  final case class JsNumber(get: Double) extends Json

  case object JsNull extends Json

  // The "serialize to JSON" behaviour is encoded in this trait
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  final case class Person(name: String, email: String)

  // 隐式转换 用来处理在参数传递的时候可以不需要写得那么的多
  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] =
      new JsonWriter[String] {
        def write(value: String): Json =
          JsString(value)
      }
    implicit val personWriter: JsonWriter[Person] =
      new JsonWriter[Person] {
        def write(value: Person): Json =
          JsObject(Map(
            "name" -> JsString(value.name),
            "email" -> JsString(value.email)
          ))
      }

    implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
      new JsonWriter[Option[A]] {
        def write(option: Option[A]): Json =
          option match {
            case Some(aValue) => writer.write(aValue)
            case None => JsNull
          }
      }

    // etc...
  }

  // 提供给用户的接口,
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }

  // 使用亡灵军团的隐式类来做简化版的隐式转换的逻辑
  object JsonSyntax {

    implicit class JsonWriterOps[A: JsonWriter](value: A) {
      def toJson: Json = implicitly[JsonWriter[A]].write(value)
    }

  }

  def main(args: Array[String]): Unit = {

    import JsonWriterInstances._
    import JsonSyntax._
    val js = Json.toJson(Person("yingliufengpeng", "2906631584@qq.com"))
    println(s"js is $js")

    val js2 = Person("yingliufengpeng", "2900").toJson
    println(s"js2 is $js2")

    val js3 = Option(Person("kkk", "kkk")).toJson
    println(s"js3 is $js3")
  }

}
