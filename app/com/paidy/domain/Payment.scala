package com.paidy.domain

import org.joda.time.DateTime

case class Payment(

  entityId: String,

  merchantId: String,

  consumerId: String,

  createdAt: DateTime,

  updatedAt: DateTime,

  // TODO: use BigDecimal instead of Int
  amount: Int,

  test: Boolean
)
