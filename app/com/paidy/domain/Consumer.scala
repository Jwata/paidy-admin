package com.paidy.domain

import java.sql.Timestamp

import org.joda.time.DateTime

case class Consumer(

  entityId: String,

  // TODO: use Consumer.Status instead of String
  status: String,

  name: Option[String],

  email: String,

  phone: String,

  // TODO: use org.joda.time.DateTime instead of Timestamp
  createdAt: Timestamp,

  // TODO: use org.joda.time.DateTime instead of Timestamp
  updatedAt: Timestamp,

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
