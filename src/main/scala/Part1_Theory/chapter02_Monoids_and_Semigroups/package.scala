package object chapter02_Monoids_and_Semigroups {

 object MM {

   def ff(args: Array[String]): Unit = {
     val m = "" + "4" // 更像是内置类型
     val n = "" ++ "44" // 更像是方法的调用

     println(s"m is $m, n is $n")
   }
 }

}
