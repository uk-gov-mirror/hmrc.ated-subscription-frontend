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

import config.WSHttp
import models.{AgentEmail, ClientDisplayName, Mandate}
import play.api.Logger
import play.api.mvc.Request
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.frontend.auth.AuthContext
import uk.gov.hmrc.play.frontend.filters.SessionCookieCryptoFilter
import uk.gov.hmrc.play.http.HttpGet
import uk.gov.hmrc.play.partials.HeaderCarrierForPartialsConverter

import scala.concurrent.Future

trait AgentClientMandateFrontendConnector extends ServicesConfig with RawResponseReads with HeaderCarrierForPartialsConverter {

  def serviceUrl = baseUrl("agent-client-mandate-frontend")
  val emailUri = "mandate/agent/email-session"
  val displayNameUri = "mandate/agent/client-display-name-session"
  val mandateDetails = "mandate/agent/old-nonuk-mandate-from-session"
  val service = "ATED"
  val http: HttpGet

  def getAgentEmail(implicit request: Request[_], user: AuthContext): Future[Option[AgentEmail]] = {
    val getUrl = s"$serviceUrl/$emailUri/$service"
    http.GET[Option[AgentEmail]](getUrl)
  }

  def getClientDisplayName(implicit request: Request[_], user: AuthContext): Future[Option[ClientDisplayName]] = {
    val getUrl = s"$serviceUrl/$displayNameUri/$service"
    http.GET[Option[ClientDisplayName]](getUrl)
  }

  def getOldMandateDetails(implicit request: Request[_], user: AuthContext): Future[Option[Mandate]] = {
    val getUrl = s"$serviceUrl/$mandateDetails/$service"
    http.GET[Option[Mandate]](getUrl)
  }

}

object AgentClientMandateFrontendConnector extends AgentClientMandateFrontendConnector {
  // $COVERAGE-OFF$
  val http = WSHttp
  override def crypto: (String) => String = SessionCookieCryptoFilter.encrypt _
  // $COVERAGE-ON$
}
