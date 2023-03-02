package com.knoldus.bootstrap

import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.knoldus.bootstrap.CORSSupport.{handleCORS, handleErrors}
import com.knoldus.routes.BaseRoutes
import com.knoldus.routes.health.HealthCheckRoutes
import com.knoldus.routes.promotion.PromotionRoutes
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext

class RoutesInstantiator(config: Config, services: ServiceInstantiator)(implicit
  val ec: ExecutionContext,
  val mat: Materializer,
  val logger: LoggingAdapter
) {

  private val promotionRoutes = new PromotionRoutes(config, services.promotionService)
  private val healthCheckRoutes = new HealthCheckRoutes(config, services.healthCheckService)

  val routes: Route = handleErrors {
    handleCORS {
      BaseRoutes.seal {
        ignoreTrailingSlash {
          BaseRoutes.logRequestResponse() {
            concat(
              promotionRoutes.routes,
              healthCheckRoutes.routes
            )
          }
        }
      }
    }
  }

}
