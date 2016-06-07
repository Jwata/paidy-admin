package com.paidy.datastorage.slick

import org.joda.time.DateTime

import com.paidy.datastorage.ConsumerRepository
import com.paidy.domain.Consumer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait SlickConsumerRepository extends SlickBaseRepository {

  import dbConfig.driver.api._

  class ConsumersTable(tag: Tag) extends Table[Consumer](tag, "consumers") with SlickTimestampMapper {

    implicit def statusMapper = MappedColumnType.base[Consumer.Status, String](_.toString, stringToStatus)

    def stringToStatus(string: String) = Consumer.Status.fromString(string)

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
      val byIdQuery = consumers.filter(_.entityId === entityId).take(1).result
      db.run(byIdQuery).map(_.headOption)
    }

    override def list(offset: Int = 0, limit: Int = 100, status: Option[Consumer.Status] = None): Future[List[Consumer]] = {
      // TODO: order by DateTime using db query, fetching all data and sort them for now
      // TODO; filter by Consumer.Status using db query
      val listQueryResult = status match {
        case Some(s) => db.run(consumers.result).map(l => l.filter(_.status == s))
        case None => db.run(consumers.result)
      }
      import com.github.nscala_time.time.Imports.DateTimeOrdering
      listQueryResult.map(_.toList.sortBy(_.createdAt).reverse.drop(offset).take(limit))
    }

    override def count: Future[Long] = {
      val countQuery = consumers.length
      db.run(countQuery.result).map(_.toLong)
    }
  }

}
