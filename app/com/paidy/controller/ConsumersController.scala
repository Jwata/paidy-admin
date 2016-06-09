package com.paidy.controllers

import javax.inject._
import akka.actor.ActorSystem

import com.paidy.domain.Consumer
import com.paidy.service.ConsumerService

import play.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ConsumersController @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  lazy val consumerService = new ConsumerService(DatabaseConfigProvider.get[JdbcProfile](Play.current))

  def overview(statusParam: Option[String]) = Action.async {
    val status = statusParam.map { s => paramToConsumerStatus(s) }.getOrElse(None)
    consumerService.listWithPaymentSummary(status).map {
      list => Ok(views.html.overview(list))
    }
  }

  def disable(entityId: String) = Action.async { request =>
    consumerService.disableById(entityId).map {
      result => Redirect("/consumers/overview")
    }
  }

  private def paramToConsumerStatus(string: String): Option[Consumer.Status] = {
    try {
      Some(Consumer.Status.fromString(string))
    } catch {
      case _ => None
    }
  }
}
