package com.paidy.domain

import java.sql.Timestamp

import org.joda.time.DateTime

case class Payment(

  entityId: String,

  merchantId: String,

  consumerId: String,

  // TODO: use org.joda.time.DateTime instead of Timestamp
  createdAt: Timestamp,

  // TODO: use org.joda.time.DateTime instead of Timestamp
  updatedAt: Timestamp,

  // TODO: use BigDecimal instead of Int
  amount: Int,

  test: Boolean
)
