package com.paidy.domain

import org.joda.time.DateTime

case class Consumer(

  entityId: String,

  // TODO: use Consumer.Status instead of String
  status: String,

  name: Option[String],

  email: String,

  phone: String,

  createdAt: DateTime,

  updatedAt: DateTime,

  test: Boolean

)

//object Consumer {
//
//  sealed trait Status
//
//  object Status {
//
//    case object Enabled extends Status
//
//    case object Disabled extends Status
//
//  }
//
//}
