package com.paidy.service

import com.paidy.datastorage.slick.ConsumerRepositorySlick
import com.paidy.domain.Consumer

import scala.concurrent.Future

object ConsumerService {
  def list: Future[List[Consumer]] = ConsumerRepositorySlick.list()
}