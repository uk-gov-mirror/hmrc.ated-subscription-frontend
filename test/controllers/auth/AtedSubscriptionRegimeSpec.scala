/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.auth

import controllers.auth.AtedSubscriptionRegime._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.play.frontend.auth.connectors.domain.Accounts

import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}

class AtedSubscriptionRegimeSpec extends PlaySpec with OneServerPerSuite with MockitoSugar {

  "AtedSubscriptionRegime" must {

    "define isAuthorised" must {

      val accounts = mock[Accounts](RETURNS_DEEP_STUBS)

      "return true when the user as an agent" in {
        when(accounts.agent.isDefined).thenReturn(true)
        isAuthorised(accounts) must be(true)
      }

      "return true when the user is org" in {
        when(accounts.org.isDefined).thenReturn(true)
        isAuthorised(accounts) must be(true)
      }

      "return false when the user is not registered as an agent or org" in {
        when(accounts.agent.isDefined).thenReturn(false)
        when(accounts.org.isDefined).thenReturn(false)
        isAuthorised(accounts) must be(false)
      }

    }

    "define the authentication type as the Ated Subscription GG" in {
      authenticationType must be(AtedSubscriptionGovernmentGateway)
    }

    "define the unauthorised landing page as /unauthorised" in {
      unauthorisedLandingPage.get must be("/ated-subscription/unauthorised")
    }

  }
  
}
