package com.knoldus.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.models.PromotionStatus.PromotionStatus
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsString, JsValue, RootJsonFormat}

import java.sql.Timestamp
import java.time.Instant

final case class BaseResponse(status: Boolean)

final case class ErrorResponse( error: String,
                                message: Option[String]
                              )

final case class PingResponse(message: String)

final case class PromotionStatusResponse(promotionStatus: PromotionStatus)

object HttpProtocols extends SprayJsonSupport with DefaultJsonProtocol{

  implicit def enumFormat[T <: Enumeration](implicit enu: T): RootJsonFormat[T#Value] =
    new RootJsonFormat[T#Value] {
      def write(obj: T#Value): JsValue = JsString(obj.toString)

      def read(json: JsValue): T#Value =
        json match {
          case JsString(txt) => enu.withName(txt)
          case somethingElse =>
            throw DeserializationException(s"Expected a value from enum $enu instead of $somethingElse")
        }
    }

  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {

    override def write(obj: Timestamp): JsValue = JsNumber(obj.getTime)

    override def read(json: JsValue): Timestamp =
      json match {
        case JsString(s) => Timestamp.from(Instant.ofEpochMilli(s.toLong))
        case _ => throw new DeserializationException("Error info you want here ...")
      }
  }
  implicit val ErrorResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse)
  implicit val PingResponseFormat: RootJsonFormat[PingResponse] = jsonFormat1(PingResponse)
  implicit val promotionStatusFormat: RootJsonFormat[PromotionStatus] = enumFormat(PromotionStatus)
  implicit val PromotionStatusResponseFormat: RootJsonFormat[PromotionStatusResponse] = jsonFormat1(
    PromotionStatusResponse
  )
  implicit val healthCheckRequestFormat : RootJsonFormat[HealthCheckRequest] = jsonFormat1(HealthCheckRequest)
  implicit val baseResponseFormat: RootJsonFormat[BaseResponse] = jsonFormat1(BaseResponse)

}
