package com.knoldus.routes.health

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.knoldus.routes.BaseRoutes
import com.knoldus.service.health.HealthCheckService
import com.typesafe.config.Config
import com.knoldus.routes.AuthorizationRoutes
import com.knoldus.models.HttpProtocols._

import scala.concurrent.ExecutionContext

class HealthCheckRoutes(val conf: Config, healthCheckService: HealthCheckService)
                       (implicit
                        val ec: ExecutionContext,
                        val mat: Materializer
                       ) extends BaseRoutes with AuthorizationRoutes{
  val routes : Route =
    pathPrefix("health") {
      authenticate {
        get{
          onSuccess(healthCheckService.checkHealth()) {
            case Right(response) =>
              complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(MediaTypes.`application/json`, response.status.toString)))
            case Left(response) =>
              complete(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity(MediaTypes.`application/json`, response.toString)))
          }
        }
      }
    }


}
