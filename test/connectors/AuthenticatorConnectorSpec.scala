/*
 * Copyright 2017 HM Revenue & Customs
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

import builders.AuthBuilder
import org.mockito.Matchers
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.play.http._
import uk.gov.hmrc.play.http.ws.{WSGet, WSPost}

import scala.concurrent.Future
import uk.gov.hmrc.http._
import uk.gov.hmrc.http.logging.SessionId


class AuthenticatorConnectorSpec extends PlaySpec with OneServerPerSuite with MockitoSugar with BeforeAndAfterEach {

  trait MockedVerbs extends CoreGet with CorePost
  val mockWSHttp = mock[MockedVerbs]

  object TestAuthenticatorConnector extends AuthenticatorConnector {
    override val http: CoreGet with CorePost = mockWSHttp
  }

  override def beforeEach = {
    reset(mockWSHttp)
  }

  "AuthenticatorConnector" must {
    val subscribeFailureResponseJson = Json.parse( """{"reason" : "Error happened"}""")
    implicit val hc = new HeaderCarrier(sessionId = Some(SessionId(s"session-${UUID.randomUUID}")))
    implicit val user = AuthBuilder.createUserAuthContext("User-Id", "name")

    "refresh user" must {
      "works for a user" in {

        when(mockWSHttp.POSTEmpty[HttpResponse](Matchers.any())(Matchers.any(), Matchers.any(), Matchers.any())).
          thenReturn(Future.successful(HttpResponse(OK, responseJson = None)))

        val result = TestAuthenticatorConnector.refreshProfile()
        val enrolResponse = await(result)
        enrolResponse.status must be(OK)
      }
    }
  }
}
