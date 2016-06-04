package com.paidy.datastorage.slick

import java.sql.Timestamp

import com.paidy.datastorage.ConsumerRepository
import com.paidy.domain.Consumer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait SlickConsumerRepository extends SlickBaseRepository {

  import dbConfig.driver.api._

  class ConsumersTable(tag: Tag) extends Table[Consumer](tag, "consumers") {

    def * = (entityId, status, name.?, email, phone, createdAt, updateAt, test) <>(Consumer.tupled, Consumer.unapply)

    def entityId = column[String]("entity_id", O.PrimaryKey)

    def status = column[String]("status")

    def name = column[String]("name")

    def email = column[String]("email")

    def phone = column[String]("phone")

    def createdAt = column[Timestamp]("created_at")

    def updateAt = column[Timestamp]("created_at")

    def test = column[Boolean]("test")

  }

  object ConsumerRepository extends ConsumerRepository {
    lazy val consumers = TableQuery[ConsumersTable]

    override def byId(entityId: String): Future[Option[Consumer]] = {
      val byIdQuery = consumers.filter(_.entityId === entityId).take(1).result
      db.run(byIdQuery).map(_.headOption)
    }

    override def list(offset: Int = 0, limit: Int = 100): Future[List[Consumer]] = {
      val listQuery = consumers.drop(offset).take(limit).result
      db.run(listQuery).map(_.toList)
    }

    override def count: Future[Long] = {
      val countQuery = consumers.length.result
      db.run(countQuery).map(_.toLong)
    }
  }

}
