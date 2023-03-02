package com.knoldus.routes.promotion

import akka.http.scaladsl.server.Directives.{get, path, pathPrefix}
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.knoldus.routes.{AuthorizationRoutes, BaseRoutes}
import com.typesafe.config.Config
import com.knoldus.models.HttpProtocols._
import com.knoldus.service.promotion.PromotionService

import scala.concurrent.ExecutionContext

class PromotionRoutes(val conf: Config, promotionService: PromotionService)(implicit
  val ec: ExecutionContext,
  val mat: Materializer
) extends BaseRoutes
    with AuthorizationRoutes {

  val routes: Route =
    authenticate {
      pathPrefix("promotion") {
        path("period") {
          get {
            Ok(promotionService.getPromotionStatus)
          }
        }
      }
    }

}
