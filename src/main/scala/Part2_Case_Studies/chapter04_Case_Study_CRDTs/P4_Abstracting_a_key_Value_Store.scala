package Part2_Case_Studies.chapter04_Case_Study_CRDTs

object P4_Abstracting_a_key_Value_Store {

  def main(args: Array[String]): Unit = {

    trait KeyValueStore[F[_, _]] {

      def put[K, V](f: F[K, V])(k: K, v: V): F[K, V]

      def get[K, V](f: F[K, V])(k: K): Option[V]

      def getOrElse[K, V](f: F[K, V])(k: K, default: V): V =
        get(f)(k).getOrElse(default)

      def values[K, V](f: F[K, V]): List[V]

    }
  }
}
