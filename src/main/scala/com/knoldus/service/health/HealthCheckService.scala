package com.knoldus.service.health

import com.knoldus.dao.HealthCheckRepository
import com.knoldus.models.{BaseResponse, ErrorResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HealthCheckService (healthCheckRepository : HealthCheckRepository) {

  def checkHealth (): Future[Either[ErrorResponse, BaseResponse]] = {
    healthCheckRepository.update().map{
      case true => Right(BaseResponse(true))
      case false => Left(ErrorResponse("error", None))
    }
  }
}
