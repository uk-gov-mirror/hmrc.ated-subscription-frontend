/*
 * Copyright 2021 HM Revenue & Customs
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

import config.ApplicationConfig
import javax.inject.Inject
import play.api.mvc.Request
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient
import uk.gov.hmrc.play.partials.HeaderCarrierForPartialsConverter

import scala.concurrent.{ExecutionContext, Future}

class BusinessCustomerFrontendConnector  @Inject()(appConfig: ApplicationConfig,
                                                   http: DefaultHttpClient)
  extends RawResponseReads with HeaderCarrierForPartialsConverter {

  val serviceUrl: String = appConfig.serviceUrlBC
  val businessCustomerUri = "business-customer"
  val reviewDetailsUri = "fetch-review-details"
  val service = "ATED"

  override def crypto: String => String = identity

  def getBusinessCustomerDetails(implicit request: Request[_], ec: ExecutionContext): Future[HttpResponse] = {
    val getUrl = s"$serviceUrl/$businessCustomerUri/$reviewDetailsUri/$service"
    http.GET(getUrl)
  }
}
