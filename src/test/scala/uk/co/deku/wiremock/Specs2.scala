package uk.co.deku.wiremock

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import org.specs2.mutable.{ BeforeAfter, Specification }
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import dispatch.{ Http, url }

class Specs2 extends Specification {

  val Port = 8080
  val Host = "localhost"

  trait StubServer extends BeforeAfter {
    val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

    def before = {
      wireMockServer.start()
      WireMock.configureFor(Host, Port)
    }

    def after = wireMockServer.stop()
  }
  
  "WireMock" should {
    "stub get request" in new StubServer {
      val path = "/my/resource"
      stubFor(get(urlEqualTo(path))
        .willReturn(
          aResponse()
            .withStatus(200)))

      val request = url(s"http://$Host:$Port$path").GET
      val responseFuture = Http(request)

      val response = Await.result(responseFuture, Duration(100, TimeUnit.MILLISECONDS))
      response.getStatusCode mustEqual 200
    }
  }
}