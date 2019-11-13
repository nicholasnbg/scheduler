package com.nbgdev.scheduler.model


import java.util.UUID

import com.nbgdev.scheduler.model.ZoneModel.Id

import scala.concurrent.duration.Duration

case class Commute(zoneName: String, duration: Duration)

object ZoneModel {
  type Id = String
  final case class ZoneId(value: String)
}

case class Zone(id: Id = UUID.randomUUID().toString, name: String, commutes: Vector[Commute])

