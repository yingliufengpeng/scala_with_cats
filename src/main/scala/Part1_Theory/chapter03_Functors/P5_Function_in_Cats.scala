package Part1_Theory.chapter03_Functors

import cats.Functor

import scala.concurrent.ExecutionContext.Implicits.global._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object P5_Function_in_Cats {
  def main(args: Array[String]): Unit = {

    import cats.Functor
    import cats.instances.list._ // for Functor
    import cats.instances.option._ // for Functor

    val list1 = List(1, 2, 3)
    val list2 = Functor[List].map(list1)(_ * 2)
    println(s"list1 is $list1, list2 is $list2")

    val option1 = Option(123)
    val option2 = Functor[Option].map(option1)(_.toString)
    println(s"option1 is $option1, option2 is $option2")

    val func = (x: Int) => x + 1
    // func: Int => Int = <function1>
    val liftedFunc = Functor[Option].lift(func)
    //  liftedFunc: Option[Int] => Option[Int] ....
    val r = liftedFunc(Option(2))
    println(s"r is $r")

    import cats.instances.function._ // for Functor
    import cats.syntax.functor._ // for map
    val func1 = (a: Int) => a + 1
    val func2 = (a: Int) => a * 2
    val func3 = (a: Int) => a + "!"
    val func4 = func1.map(func2).map(func3)
    val r2 = func4(123)
    println(s"r2 is $r2")

    def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] = start.map(n => n * 1 * 2)

    import cats.instances.option._ // for Functor
    import cats.instances.list._ // for Functor
    val r3 = doMath(Option(20))
    println(s"r3 is $r3")
    val r4 = doMath(List(1, 2, 3))
    println(s"r4 is $r4")

    // Sometimes we need to inject dependencies into our instances
    implicit def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
      new Functor[Future] {
        override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
      }
    import cats.instances.future._
    import scala.concurrent.ExecutionContext.Implicits._
    val ffuntor = Functor[Future]
    val r5 = ffuntor.map(Future{33})(n => n + 2) // 编译器自己推导
    println(s"r5 is ${Await.result(r5, Duration.MinusInf)}")

  }


}
