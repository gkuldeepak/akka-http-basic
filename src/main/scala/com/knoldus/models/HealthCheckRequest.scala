package com.knoldus.models

import java.sql.Timestamp
import com.knoldus.models.TimeFormat._

case class HealthCheckRequest (updated: Timestamp = new Timestamp(now.getTime))
