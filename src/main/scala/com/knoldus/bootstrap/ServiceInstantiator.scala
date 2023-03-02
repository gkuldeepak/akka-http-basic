package com.knoldus.bootstrap

import akka.actor.Scheduler
import akka.actor.typed.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.Materializer
import com.knoldus.command.Command
import com.knoldus.service.health.HealthCheckService
import com.knoldus.service.promotion.PromotionService
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext

class ServiceInstantiator(conf: Config, repositoryInstantiator: RepositoryInstantiator)(implicit
  ec: ExecutionContext,
  log: LoggingAdapter,
  materializer: Materializer,
  scheduler: Scheduler,
  system: ActorSystem[Command]
) {

  lazy val promotionService = new PromotionService(conf.getConfig("promotion-details"))
  lazy val healthCheckService = new HealthCheckService(repositoryInstantiator.healthCheckRepository)
}
