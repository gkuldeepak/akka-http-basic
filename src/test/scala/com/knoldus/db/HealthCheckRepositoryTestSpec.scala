package com.knoldus.db

import com.knoldus.BaseSpec
import com.knoldus.models.{HealthCheckRequest, TimeFormat}
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.testcontainers.containers.MySQLContainer
import slick.jdbc.MySQLProfile.api._
import slick.util.AsyncExecutor

import java.sql.Timestamp
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class HealthCheckRepositoryWithTestAPI (db: Database)(implicit ec: ExecutionContext) extends HealthCheckRepositorySqlImpl(db) {

  def store(): Future[Boolean] ={
    val time  = new Timestamp(System.currentTimeMillis())
    db.run(this.map(_.health) += time) map (_ > 0)
  }

  def createTable() {
    val schema = this.schema
    Await.result(db.run(schema.createIfNotExists), Duration.Inf)
  }

  def dropTable() {
    val schema = this.schema
    Await.result(db.run(schema.dropIfExists), Duration.Inf)
  }
}

class HealthCheckRepositoryTestSpec extends BaseSpec {

  behavior of s"${this.getClass.getSimpleName}"

  val dbConfig: Config = conf.getConfig("db")
  val url: String = dbConfig.getString("url")
  val user: String = dbConfig.getString("user")
  val password: String = dbConfig.getString("password")
  val dbMaxConnection: Int = dbConfig.getInt("max-connections")
  val defaultDockerImageName = s"mysql:latest"
  val container: MySQLContainer[_] = new MySQLContainer(defaultDockerImageName)
  private val DEFAULT_CONNECTION_TIMEOUT: Int = 5000
  private val DEFAULT_QUEUE_SIZE: Int = 1000

  container.withUsername(user)
  container.withPassword(password)
  container.withDatabaseName("sample")
  var db: Option[Database] = None

  override def beforeAll() {
    container.start()

    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(container.getJdbcUrl)
    hikariConfig.setUsername(user)
    hikariConfig.setPassword(password)
    hikariConfig.setDriverClassName(dbConfig.getString("driver"))
    hikariConfig.setMaximumPoolSize(dbMaxConnection)
    hikariConfig.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT)

    db = Some(
      Database.forDataSource(
        new HikariDataSource(hikariConfig),
        Some(dbMaxConnection),
        AsyncExecutor.apply(
          "slick-microsite-async-executor",
          dbMaxConnection,
          dbMaxConnection,
          DEFAULT_QUEUE_SIZE,
          dbMaxConnection
        )
      )
    )

    new HealthCheckRepositoryWithTestAPI(db.value).createTable()
  }

    override def afterAll() {
      new HealthCheckRepositoryWithTestAPI(db.value).dropTable()
      container.stop()
    }

  it should "not update timestamp in database" in {
    val repository = new HealthCheckRepositoryWithTestAPI(db.value)
    val result = repository.update().futureValue
    result shouldBe false
  }

  it should "store and update timestamp in database" in {
    val repository = new HealthCheckRepositoryWithTestAPI(db.value)
    repository.store().futureValue
    val result = repository.update().futureValue
    result shouldBe true
  }
}
