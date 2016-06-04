package com.paidy.domain

import java.sql.Timestamp

import org.joda.time.DateTime

case class Consumer(

  entityId: String,

  //  status: Consumer.Status,
  status: String,

  name: Option[String],

  email: String,

  phone: String,

  createdAt: Timestamp,
  //  createdAt: DateTime,

  updatedAt: Timestamp,
  //  updatedAt: DateTime,

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
