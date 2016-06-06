package com.paidy.datastorage.slick

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait SlickBaseRepository {
  val dbConfig: DatabaseConfig[JdbcProfile]
  lazy val db = dbConfig.db
}