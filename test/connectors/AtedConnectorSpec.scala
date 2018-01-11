/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package connectors

import java.util.UUID

import builders._
import models._
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost}

import scala.concurrent.Future
import uk.gov.hmrc.http._
import uk.gov.hmrc.http.logging.SessionId

class AtedConnectorSpec extends PlaySpec with OneServerPerSuite with MockitoSugar with BeforeAndAfterEach {

  trait MockedVerbs extends CoreGet with CorePost with CoreDelete
  val mockWSHttp = mock[MockedVerbs]
  val periodKey = "2015"

  object TestAtedConnector extends AtedConnector {
    override val http: CoreGet with CorePost with CoreDelete = mockWSHttp
    override val serviceURL = baseUrl("ated")
  }

  override def beforeEach = {
    reset(mockWSHttp)
  }

  "AtedConnector" must {
    import AuthBuilder._

    val periodKey = 2015

    "getDetails" must {
      "GET agent details from ETMP for a user" in {
        implicit val hc = HeaderCarrier(sessionId = Some(SessionId(s"session-${UUID.randomUUID}")))
        implicit val user = AuthBuilder.createAgentAuthContext("userId", "joe bloggs")
        when(mockWSHttp.GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Future.successful(HttpResponse(OK)))
        val result = TestAtedConnector.getDetails("ARN1234567", "arn")
        await(result).status must be(OK)
        verify(mockWSHttp, times(1)).GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())
      }

      "GET user details from ETMP for an agent" in {
        implicit val hc = HeaderCarrier(sessionId = Some(SessionId(s"session-${UUID.randomUUID}")))
        implicit val user = AuthBuilder.createAgentAuthContext("userId", "joe bloggs")
        when(mockWSHttp.GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Future.successful(HttpResponse(OK)))
        val result = TestAtedConnector.getDetails("XN1200000100001", "arn")
        await(result).status must be(OK)
        verify(mockWSHttp, times(1)).GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())
      }

      "GET subscription data from ETMP for an agent" in {
        implicit val hc = HeaderCarrier(sessionId = Some(SessionId(s"session-${UUID.randomUUID}")))
        implicit val user = AuthBuilder.createAgentAuthContext("userId", "joe bloggs")
        when(mockWSHttp.GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(Future.successful(HttpResponse(OK)))
        val result = TestAtedConnector.retrieveSubscriptionData("XN1200000100001")
        await(result).status must be(OK)
        verify(mockWSHttp, times(1)).GET[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())
      }
    }

  }
}
