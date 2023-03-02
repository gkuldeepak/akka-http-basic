package com.knoldus.services.health

import com.knoldus.BaseSpec
import com.knoldus.dao.HealthCheckRepository
import com.knoldus.models.{BaseResponse, ErrorResponse}
import com.knoldus.service.health.HealthCheckService
import org.mockito.Mockito.when


class HealthCheckServiceTest extends BaseSpec{

  behavior of s"${this.getClass.getSimpleName}"

  val healthCheckRepository: HealthCheckRepository = mock[HealthCheckRepository]
  val healthCheckService = new HealthCheckService(healthCheckRepository)

  trait Setup {
    when(healthCheckRepository.update()).thenReturn(future(true))
  }

  it should "update timestamp " in new Setup {
    val result: Either[ErrorResponse, BaseResponse] = healthCheckService.checkHealth().futureValue
    result.isRight shouldBe true
  }

  it should "not update time stamp" in new Setup {
    val result: Either[ErrorResponse, BaseResponse] = healthCheckService.checkHealth().futureValue
    result.isLeft shouldBe false
  }

}
