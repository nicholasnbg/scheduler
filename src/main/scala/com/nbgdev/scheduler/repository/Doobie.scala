package com.nbgdev.scheduler.repository

import cats.effect.IO
import doobie._
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.ExecutionContext

object Doobie {

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", "jdbc:postgresql:scheduler", "postgres", "testpass"
  )

}
