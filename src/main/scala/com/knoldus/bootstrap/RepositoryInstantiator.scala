package com.knoldus.bootstrap

import com.knoldus.dao.HealthCheckRepository

trait RepositoryInstantiator {

  val healthCheckRepository: HealthCheckRepository

}
