package com.nbgdev.scheduler.model

import scala.concurrent.duration.Duration

case class Commute(zone: Zone, duration: Duration)

case class Zone (name: String, commutes: Vector[Commute])

