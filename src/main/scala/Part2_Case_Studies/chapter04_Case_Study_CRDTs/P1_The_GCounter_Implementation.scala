package Part2_Case_Studies.chapter04_Case_Study_CRDTs

object P1_The_GCounter_Implementation {


  def main(args: Array[String]): Unit = {

    final case class GCounter(counters: Map[String, Int]) {
      def increment(machine: String, amount: Int): GCounter = {

        val newcounters = counters.map {
          case (k, v) if k == machine => (k, amount + v)
          case p => p
        }

        GCounter(newcounters)
      }

      // 这种写法可能会少数据
      def merge(that: GCounter): GCounter = {
        val m = counters.zip(that.counters).map {
          case (le, ri) => (le._1, math.max(le._2, ri._2))
        }
        GCounter(m)
      }

      def total: Int = counters.values.sum
    }
  }
}
