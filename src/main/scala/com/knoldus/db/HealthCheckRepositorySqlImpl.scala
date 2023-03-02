package com.knoldus.db

import com.knoldus.dao.HealthCheckRepository
import com.knoldus.models.HealthCheckRequest
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{ProvenShape, Rep, TableQuery, Tag}

import java.sql.Timestamp
import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HealthCheckRepositorySqlImpl(db: Database) extends TableQuery(new HealthCheckRepositoryTable(_))
  with HealthCheckRepository {

  def update(): Future[Boolean] = {
    db.run(this.map(_.health).update(new Timestamp(System.currentTimeMillis()))) map {
      size =>
        if(size > 0)
          true
        else false
    }
  }

}

class HealthCheckRepositoryTable(tag: Tag) extends Table[HealthCheckRequest](tag, "health_check"){
  def health: Rep[Timestamp] = column[Timestamp]("health", O.Default(new Timestamp(new Date().getTime)))

  def * : ProvenShape[HealthCheckRequest] = (health) <> (HealthCheckRequest.apply , HealthCheckRequest.unapply)
}
