package com.paidy.datastorage

import com.paidy.domain.Consumer

import scala.concurrent.Future

trait ConsumerRepository {

  def byId(entityId: String): Future[Option[Consumer]]

  def list(offset: Int = 0, limit: Int = 100, status: Option[String] = None): Future[List[Consumer]]

  def count: Future[Long]

}
