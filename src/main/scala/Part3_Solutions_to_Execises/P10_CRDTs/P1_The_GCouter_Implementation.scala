package Part3_Solutions_to_Execises.P10_CRDTs

object P1_The_GCouter_Implementation {

  def main(args: Array[String]): Unit = {

    final case class GCounter(counters: Map[String, Int]) {

      def increment(machine: String, amount: Int): GCounter = {
        val value = amount + counters.getOrElse(machine, 0)
        GCounter(counters + (machine -> value))
      }

      def merge(that: GCounter): GCounter =
        GCounter(that.counters ++ this.counters.map{
          case (k, v) =>
            k -> (v max that.counters.getOrElse(k, 0))
        })

      def total: Int = counters.values.sum

    }
  }

}
