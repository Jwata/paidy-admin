package com.paidy.controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class ConsumersController @Inject() extends Controller {

  def overview = Action {
    Ok("Consumers Overview")
  }

}
