package com.knoldus.service.promotion

import akka.event.LoggingAdapter
import com.knoldus.models.{PromotionStatus, PromotionStatusResponse}
import com.typesafe.config.Config

import java.time.Instant

class PromotionService(promoConf: Config)(implicit logger: LoggingAdapter) {

  private val END_OF_PROMOTION_TIMESTAMP = promoConf.getLong("end-of-promotion-timestamp")
  private val PROMOTION_LIVE_TIMESTAMP = promoConf.getLong("promotion-live-timestamp")

  def getPromotionStatus: PromotionStatusResponse = {
    val now: Long = Instant.now().toEpochMilli

    val promotionStatus = {
      if (now >= END_OF_PROMOTION_TIMESTAMP) PromotionStatus.PromotionEnded
      else if (now >= PROMOTION_LIVE_TIMESTAMP) PromotionStatus.PromotionLive
      else PromotionStatus.ComingSoon
    }
    PromotionStatusResponse(promotionStatus)
  }

}
