package com.paidy.datastorage

import com.paidy.domain.Payment

import scala.concurrent.Future

trait PaymentRepository {

  def byId(entityId: String): Future[Option[Payment]]

  def byConsumerId(consumerId: String): Future[List[Payment]]

}

