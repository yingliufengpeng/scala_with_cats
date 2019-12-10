package Part2_Case_Studies.chapter01_Case_Study_Testing_Asynchronous_Code

object Main {

  def main(args: Array[String]): Unit = {

    import scala.concurrent.Future

    // for client
    trait UptimeClient {
      def getUptime(hostname: String): Future[Int]
    }

    // for server
    import cats.instances.future._
    import cats.instances.list._
    import cats.syntax.traverse._

    import scala.concurrent.ExecutionContext.Implicits.global

    class UptimeService(client: UptimeClient) {
      def getTotalUptime(hostnams: List[String]): Future[Int] =
        hostnams.traverse(client.getUptime).map(list => list.sum)
    }

    class TestUpTimeClient(hosts: Map[String, Int]) extends UptimeClient {
      override def getUptime(hostname: String): Future[Int] =
        Future.successful(hosts.getOrElse(hostname, 0))
    }

    def testTotalUptime(): Unit = {
      val hosts = Map("host1" -> 10, "host2" -> 6)
      val client = new TestUpTimeClient(hosts)
      val service = new UptimeService(client)
      val actual = service.getTotalUptime(hosts.keys.toList)
      val expected = hosts.values.sum

      println(s"actual is $actual, expected is $expected")
    }

    testTotalUptime()

  }
}
