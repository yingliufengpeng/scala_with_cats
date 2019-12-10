package Part3_Solutions_to_Execises.P10_CRDTs

import cats.kernel.CommutativeMonoid

object P4_Abstracting_a_key_Value_Store {

  def main(args: Array[String]): Unit = {

    trait GCounter[F[_, _], K, V] {

      def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V]

      def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V]

      def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V
    }

    trait BoundedSemiLattice[A] extends CommutativeMonoid[A] {
      def combine(a1: A, a2: A): A

      def empty: A
    }

    trait KeyValueStore[F[_, _]] {

      def put[K, V](f: F[K, V])(k: K, v: V): F[K, V]

      def get[K, V](f: F[K, V])(k: K): Option[V]

      def getOrElse[K, V](f: F[K, V])(k: K, default: V): V =
        get(f)(k).getOrElse(default)

      def values[K, V](f: F[K, V]): List[V]

    }

    object KeyValueStore {

      object implicits {

        implicit val mapInstance: KeyValueStore[Map] = new KeyValueStore[Map] {
          override def put[K, V](map: Map[K, V])(k: K, v: V): Map[K, V] = map + (k -> v)

          override def get[K, V](map: Map[K, V])(k: K): Option[V] = map.get(k)

          override def values[K, V](map: Map[K, V]): List[V] = map.values.toList
        }

        import syntax22._
        import cats.syntax.semigroup._
        import cats.syntax.foldable._ // for combineAll
        import cats.instances.list._


        def m[V](v1: V, v2: V)(implicit ev: CommutativeMonoid[V]): V = v1 |+| v2

        implicit def gcounterInstance[F[_, _], K, V](implicit kvs: KeyValueStore[F], km: CommutativeMonoid[F[K, V]]): GCounter[F, K, V] =
          new GCounter[F, K, V] {
            override def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V] = f.put(k, v)

            override def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V] = f1 |+| f2

            override def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V = f.values.combineAll
          }


      }

      object syntax22 {

        implicit class KvsOps[F[_, _], K, V](f: F[K, V])(implicit kvs: KeyValueStore[F]) {

          def put(k: K, v: V): F[K, V] = kvs.put(f)(k, v)

          def get(k: K)(implicit kvs: KeyValueStore[F]): Option[V] = kvs.get(f)(k)

          def getOrElse(k: K, default: V)(implicit kvs: KeyValueStore[F]): V = kvs.getOrElse(f)(k, default)

          def values(implicit kvs: KeyValueStore[F]): List[V] = kvs.values(f)
        }

      }

    }


  }

}
