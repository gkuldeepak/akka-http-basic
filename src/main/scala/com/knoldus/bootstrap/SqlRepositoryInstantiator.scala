package com.knoldus.bootstrap

import com.knoldus.dao.{HealthCheckRepository}
import com.knoldus.db.HealthCheckRepositorySqlImpl
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.jdbc.MySQLProfile.api._
import slick.util.AsyncExecutor

import scala.concurrent.ExecutionContext

class SqlRepositoryInstantiator(dbConfig: Config)(implicit ec: ExecutionContext) extends RepositoryInstantiator {

  private val DEFAULT_CONNECTION_TIMEOUT: Int = 5000
  private val DEFAULT_QUEUE_SIZE: Int = 1000

  private val dbMaxConnection: Int = dbConfig.getInt("max-connections")

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(dbConfig.getString("url"))
  hikariConfig.setUsername(dbConfig.getString("user"))
  hikariConfig.setPassword(dbConfig.getString("password"))
  hikariConfig.setDriverClassName(dbConfig.getString("driver"))
  hikariConfig.setMaximumPoolSize(dbMaxConnection)
  hikariConfig.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT)

  val dataSource = new HikariDataSource(hikariConfig)

  private val db: Database = Database.forDataSource(
    dataSource,
    Some(dbMaxConnection),
    AsyncExecutor
      .apply("slick-microsite-async-executor", dbMaxConnection, dbMaxConnection, DEFAULT_QUEUE_SIZE, dbMaxConnection)
  )

  override val healthCheckRepository : HealthCheckRepository = new HealthCheckRepositorySqlImpl(db)

}
