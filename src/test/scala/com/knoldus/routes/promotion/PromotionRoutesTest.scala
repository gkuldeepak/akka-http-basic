package com.knoldus.routes.promotion

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.knoldus.models.{PromotionStatus, PromotionStatusResponse}
import com.knoldus.routes.RouteSpec
import com.knoldus.service.promotion.PromotionService
import org.mockito.Mockito.when
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import com.knoldus.models.HttpProtocols._

class PromotionRoutesTest extends RouteSpec with SprayJsonSupport with DefaultJsonProtocol {

  behavior of s"${this.getClass.getSimpleName}"

  val promotionService: PromotionService = mock[PromotionService]
  override val routes: Route = new PromotionRoutes(conf, promotionService).routes

  implicit val PromotionStatusResponseFormat: RootJsonFormat[PromotionStatusResponse] = jsonFormat1(
    PromotionStatusResponse
  )

  trait Setup {
    when(promotionService.getPromotionStatus).thenReturn(PromotionStatusResponse(PromotionStatus.ComingSoon))
  }

  it should "execute GET /promotion/period endpoint" in new Setup {
    Get("/promotion/period").signed.check {
      status shouldBe StatusCodes.OK
      responseAs[PromotionStatusResponse].promotionStatus should be(PromotionStatus.ComingSoon)
    }
  }
}
