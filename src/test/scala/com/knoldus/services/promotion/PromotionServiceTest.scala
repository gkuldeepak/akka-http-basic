package com.knoldus.services.promotion

import com.knoldus.BaseSpec
import com.knoldus.models.{PromotionStatus, PromotionStatusResponse}
import com.knoldus.service.promotion.PromotionService
import com.typesafe.config.{Config, ConfigValueFactory}

import java.util
import scala.jdk.CollectionConverters._

class PromotionServiceTest extends BaseSpec {

  behavior of s"${this.getClass.getSimpleName}"

  it should "get the promotion return coming soon" in {
    val configMap: util.Map[String, String] = Map(
      "promotion-live-timestamp" -> s"${Long.MaxValue}",
      "end-of-promotion-timestamp" -> s"${Long.MaxValue}"
    ).asJava

    val updatedConf: Config = conf.withValue("promotion-details", ConfigValueFactory.fromMap(configMap))

    val promoConfig: Config = updatedConf.getConfig("promotion-details")

    val promotionService = new PromotionService(promoConfig)

    promotionService.getPromotionStatus should be (PromotionStatusResponse(PromotionStatus.ComingSoon))
  }

  it should "get the promotion return Promotion is live" in {
    val configMap: util.Map[String, String] = Map(
      "promotion-live-timestamp" -> s"1628778649773",
      "end-of-promotion-timestamp" -> s"${Long.MaxValue}"
    ).asJava

    val updatedConf: Config = conf.withValue("promotion-details", ConfigValueFactory.fromMap(configMap))

    val promoConfig: Config = updatedConf.getConfig("promotion-details")

    val promotionService = new PromotionService(promoConfig)

    promotionService.getPromotionStatus should be (PromotionStatusResponse(PromotionStatus.PromotionLive))
  }

  it should "get the promotion return Promotion ended" in {
    val configMap: util.Map[String, String] = Map(
      "promotion-live-timestamp" -> s"1628778649773",
      "end-of-promotion-timestamp" -> s"1628778649773"
    ).asJava

    val updatedConf: Config = conf.withValue("promotion-details", ConfigValueFactory.fromMap(configMap))

    val promoConfig: Config = updatedConf.getConfig("promotion-details")

    val promotionService = new PromotionService(promoConfig)

    promotionService.getPromotionStatus should be (PromotionStatusResponse(PromotionStatus.PromotionEnded))
  }

}
