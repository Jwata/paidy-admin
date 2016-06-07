package com.paidy.datastorage.slick

import java.sql.Timestamp

import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait SlickBaseRepository {
  val dbConfig: DatabaseConfig[JdbcProfile]
  lazy val db = dbConfig.db

  trait SlickTimestampMapper {

    import dbConfig.driver.api._

    implicit def timestampMapper = MappedColumnType.base[DateTime, Timestamp](dateTimeToSqlTimestamp, sqlTimestampToDateTime)

    def dateTimeToSqlTimestamp: DateTime => Timestamp = { dt => new Timestamp(dt.getMillis) }

    def sqlTimestampToDateTime: Timestamp => DateTime = { ts => new DateTime(ts.getTime) }

  }

}

