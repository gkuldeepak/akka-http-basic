package com.knoldus.models

import java.util.Date

trait TimeFormat {
  def now: Date = new Date(nowMillis)

  def nowMillis: Long = System.currentTimeMillis()
}

object TimeFormat extends TimeFormat