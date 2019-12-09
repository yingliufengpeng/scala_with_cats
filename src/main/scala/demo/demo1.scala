package demo

object demo1 {

  def main(args: Array[String]): Unit = {

    trait Functor[A,+M[_]] { //
      def map2[B](f: A => B): M[B]
    }
    object Functor { //
      implicit class SeqFunctor[A](seq: Seq[A]) extends Functor[A,Seq] {
        def map2[B](f: A => B): Seq[B] = seq map f
      }
      implicit class OptionFunctor[A](opt: Option[A]) extends Functor[A,Option] {
        def map2[B](f: A => B): Option[B] = opt map f
      }

      implicit class MapFunctor[K,V1](mapKV1: Map[K,V1])
        extends Functor[V1,Map[K, ?]] {
        def map2[V2](f: V1 => V2): Map[K,V2] = mapKV1 map {
          case (k,v) => (k,f(v))
        }
      }
    }



  }

}
