package Part1_Theory.chapter03_Functors
import cats.Functor
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map
object p8_Aside_Partial_Unification {

  def main(args: Array[String]): Unit = {

    val func1 = (x: Int) => (x + 1).toDouble
    val func2 = (y: Double) => y * 2

    // 在这里就开始把func看成是functor的类型,但是默认的编译的情况下是不能够
    // 通过这些map的隐式转换,因为Functor[F[_]]接受的参数是一个,而func1的type class
    // 是Function1[-A, +B]的类型,所以需要编译器对函数类型要做相应的转换的操作
    // scalacOptions += "-Ypartial-unification"  所以需要添加编译器相关的操作逻辑!!!
    val func3 = func1.map(func2)
    val r = func3(10)
    println(s"r is $r")

    val either: Either[String, Int] = Right(123)
    val r2 = either.map(_ + 1)
    println(s"r2 is $r2")

    val func3a: Int => Double = a => func2(func1(a))
    val func3b: Int => Double = func2.compose(func1)

    import cats.syntax.contravariant._ // for contramap
//    val func3c = func2.contramap(func1) // 编译器代码寻找类型推断推理逻辑的思考
    type <=[B, A] = A => B
    // 这两种写法是等价的写法
    type F[A] = Double <= A
    type FF[A] = <=[Double, A]
    // 这两种写法也是等价
    val func2b: Double <= Double = func2
    val func2bb: <=[Double, Double] = func2
    val func3c = func2b.contramap(func1)
    val r3 = func3c(30)
    println(s"r3 is $r3")

    /**
     * The problem here is that the Contravariant for Function1 fixes the return type and leaves
     * the parameter varying, requriring the compiler to eliminate type parameters form right to left,
     * as shown below and in Figure 3.7:
     *
     * type F[A] = A => Double // 相当于是后面的类型被推断出来
     */

  }

}
