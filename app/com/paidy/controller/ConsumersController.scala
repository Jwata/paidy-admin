package com.paidy.controllers

import javax.inject._
import akka.actor.ActorSystem
import com.paidy.service.ConsumerService
import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class ConsumersController @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  def overview = Action.async {
    ConsumerService.list.map { list => Ok(list.toString) }
  }

}
