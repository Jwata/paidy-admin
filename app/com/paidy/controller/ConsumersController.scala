package com.paidy.controllers

import javax.inject._
import akka.actor.ActorSystem
import com.paidy.service.ConsumerService
import play.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ConsumersController @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  lazy val consumerService = new ConsumerService(DatabaseConfigProvider.get[JdbcProfile](Play.current))

  def overview = Action.async {
    consumerService.listWithPayments.map { list => Ok(list.toString) }
  }

}
