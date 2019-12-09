package Part1_Theory.chapter03_Functors
//import scala.language.higherKinds
object P3_Definition_of_Functor {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  def main(args: Array[String]): Unit = {

    // 模拟functor的使用逻辑

    /**
     * Functor Laws
     * Functors guarantee the same semantics whether we sequence many
     * small operations one by one, or combine them into a larger func?on
     * before mapping . To ensure this is the case the following laws must hold:
     * Identity: calling map with the identity function is the same as doing noth-
     * ing:
     * fa.map(a => a) == fa
     * Composition: mapping with two functions f and g is the same as map-
     * ping with f and then mapping with g :
     * fa.map(g(f(_))) == fa.map(f).map(g
     */
  }
}
