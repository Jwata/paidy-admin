package com.paidy.domain

import java.sql.Timestamp

import org.joda.time.DateTime

case class Payment(

  entityId: String,

  merchantId: String,

  consumerId: String,

//  createdAt: DateTime,
  createdAt: Timestamp,

//  updatedAt: DateTime,
  updatedAt: Timestamp,

  amount: BigDecimal,

  test: Boolean
)
