package uk.co.deku.wiremock

import java.util.concurrent.TimeUnit

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import org.scalatest.{ BeforeAndAfterEach, FlatSpec, Matchers }

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._

import dispatch.{ Http, url }

class ScalaTest extends FlatSpec with Matchers with BeforeAndAfterEach {

  val Port = 8080
  val Host = "localhost"
  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

  override def beforeEach {
    wireMockServer.start()
    WireMock.configureFor(Host, Port)
  }

  override def afterEach {
    wireMockServer.stop()
  }

  "WireMock" should "stub get request" in {
    val path = "/my/resource"
    stubFor(get(urlEqualTo(path))
      .willReturn(
        aResponse()
          .withStatus(200)))
    val request = url(s"http://$Host:$Port$path").GET
    val responseFuture = Http(request)

    val response = Await.result(responseFuture, Duration(100, TimeUnit.MILLISECONDS))
    response.getStatusCode should be(200)
  }

}