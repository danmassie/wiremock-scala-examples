package uk.co.deku.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.ning.http.client.Response
import dispatch.{Http, url}
import org.specs2.matcher.Matcher
import org.specs2.mutable.{Before, Specification}

import scala.concurrent.ExecutionContext.Implicits.global

class Specs2 extends Specification {
  sequential

  val Port = 8080
  val Host = "localhost"

  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

  step {
    WireMock.configureFor(Host, Port) //Must have it for any port other than 8080
    wireMockServer.start()
  }

  trait StubServer extends Before {
    def before = WireMock.reset()
    
    def haveStatus(code: Int): Matcher[Response] = ((_:Response).getStatusCode) ^^ be_==(code)
  }
  
  "WireMock" should {
    "stub get request" in new StubServer {
      val path = "/my/resource"
      stubFor(get(urlEqualTo(path))
        .willReturn(
          aResponse()
            .withStatus(200)))

      val request = url(s"http://$Host:$Port$path").GET
      Http(request) must haveStatus(200).await
    }
  }

  step {
    wireMockServer.stop()
  }
}