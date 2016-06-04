package com.paidy.service

import com.paidy.datastorage.slick._
import com.paidy.domain.Consumer
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future

class ConsumerService(val dbConfig:DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository {
  def list: Future[List[Consumer]] = ConsumerRepository.list()
}