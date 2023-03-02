package com.knoldus.routes.health

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import com.knoldus.models.{BaseResponse, ErrorResponse}
import com.knoldus.routes.RouteSpec
import com.knoldus.service.health.HealthCheckService
import org.mockito.Mockito.when
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import com.knoldus.models.HttpProtocols.{jsonFormat1, _}


class HealthCheckRoutesTest extends RouteSpec with SprayJsonSupport with DefaultJsonProtocol {

  behavior of s"${this.getClass.getSimpleName}"
  val healthService: HealthCheckService = mock[HealthCheckService]
  override val routes: Route = new HealthCheckRoutes(conf, healthService).routes

  trait Setup {
    when(healthService.checkHealth()).thenReturn(future(Right(BaseResponse(true))))
  }

  trait Setup1{
    when(healthService.checkHealth()).thenReturn(future(Left(ErrorResponse("error", None))))
  }

  it should "execute GET /health endpoint" in  new Setup {
    Get("/health").signed.check {
      status shouldBe StatusCodes.OK
      entityAs[String] shouldEqual "true"
    }
  }


  it should "not execute GET /health endpoint" in  new Setup1 {
    Get("/health").signed.check {
      status shouldBe StatusCodes.BadRequest
      entityAs[String] shouldEqual s"""ErrorResponse(error,None)""".stripMargin
    }
  }

}
