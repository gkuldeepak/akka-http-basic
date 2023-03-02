package com.knoldus.dao

import scala.concurrent.Future

trait HealthCheckRepository {
  def update() : Future[Boolean]
}
