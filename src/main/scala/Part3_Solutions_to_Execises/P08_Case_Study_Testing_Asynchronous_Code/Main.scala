package Part3_Solutions_to_Execises.P08_Case_Study_Testing_Asynchronous_Code



object Main {

  def main(args: Array[String]): Unit = {

    import cats.Id
    import scala.concurrent.Future

    trait UptimeClient[F[_]] {

      def getUptime(hostname: String): F[Int]

    }

    trait RealUptimeClient extends UptimeClient[Future] {
      override def getUptime(hostname: String): Future[Int]
    }

//    trait TestUptimeClient extends UptimeClient[Id] {
////      override def getUptime(hostname: String): Id[Int]
//      override def getUptime(hostname: String): Int
//    }

    case class TestUpTimeClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
      override def getUptime(hostname: String): Id[Int] = hosts.getOrElse(hostname, 0)
    }

    import cats.Applicative
    import cats.syntax.functor._ // for map
    import cats.syntax.traverse._
    import cats.instances.list._

    case class UptimeService[F[_]: Applicative](client: UptimeClient[F]) {
      def getTotalUptime(hostnames: List[String]): F[Int] =
        hostnames.traverse(client.getUptime).map(_.sum)
    }
    def testTotalUptime(): Unit = {
      val hosts = Map("host1" -> 10, "host2" -> 6)
      val client = TestUpTimeClient(hosts)
      val service = UptimeService(client)
      val actual = service.getTotalUptime(hosts.keys.toList)
      val expected = hosts.values.sum

      println(s"actual is $actual, expected is $expected")
    }

    testTotalUptime()

  }

}
