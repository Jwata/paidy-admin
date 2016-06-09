package com.paidy.datastorage.slick

import org.joda.time.DateTime

import com.paidy.datastorage.ConsumerRepository
import com.paidy.domain.Consumer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait SlickConsumerRepository extends SlickBaseRepository {

  import dbConfig.driver.api._

  implicit def statusMapper = MappedColumnType.base[Consumer.Status, String](_.toString, stringToStatus)

  def stringToStatus(string: String) = Consumer.Status.fromString(string)

  class ConsumersTable(tag: Tag) extends Table[Consumer](tag, "consumers") {

    def * = (entityId, status, name.?, email, phone, createdAt, updatedAt, test) <>((Consumer.apply _).tupled, Consumer.unapply)

    def entityId = column[String]("entity_id", O.PrimaryKey)

    def status = column[Consumer.Status]("status")

    def name = column[String]("name")

    def email = column[String]("email")

    def phone = column[String]("phone")

    def createdAt = column[DateTime]("created_at")

    def updatedAt = column[DateTime]("updated_at")

    def test = column[Boolean]("test")

  }

  object ConsumerRepository extends ConsumerRepository {
    lazy val consumers = TableQuery[ConsumersTable]

    override def byId(entityId: String): Future[Option[Consumer]] = {
      db.run(findByIdQuery(entityId).result).map(_.headOption)
    }

    override def disableById(entityId: String): Future[Int] = {
      val disableQuery = findByIdQuery(entityId).map(_.status).update(Consumer.Status.Disabled)
      db.run(disableQuery)
    }

    private def findByIdQuery(entityId: String): Query[ConsumersTable, ConsumersTable#TableElementType, Seq] = {
      consumers.filter(_.entityId === entityId).take(1)
    }

    override def list(offset: Int = 0, limit: Int = 100, status: Option[Consumer.Status] = None): Future[List[Consumer]] = {
      val listQueryResult = (status match {
        case Some(s) => consumers.filter(_.status === s)
        case None => consumers
      }).sortBy(_.createdAt.desc).drop(offset).take(limit).result
      db.run(listQueryResult.map(_.toList))
    }

    override def count: Future[Long] = {
      val countQuery = consumers.length.result
      db.run(countQuery).map(_.toLong)
    }

  }

}
