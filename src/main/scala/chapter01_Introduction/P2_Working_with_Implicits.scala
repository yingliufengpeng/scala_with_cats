package chapter01_Introduction

object P2_Working_with_Implicits {

  def main(args: Array[String]): Unit = {
    // 其实这个章节的难点则是在于递归的隐式搜索的逻辑有些复杂

    //Json.toJson(Option("A string"))
    // Json.toJson(Option("A string"))(optionWriter[String])
    // Json.toJson(Option("A string"))(optionWriter(stringWriter)) 然后这些隐式转换也就处理成功!!!

  }

}
