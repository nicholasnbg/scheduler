package com.nbgdev.scheduler.model
import java.util.UUID

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Commute(endZoneId: UUID, duration: Int)

object Commute {
  implicit val encoder: Encoder[Commute] = deriveEncoder[Commute]
  implicit val decoder: Decoder[Commute] = deriveDecoder[Commute]
}

case class Zone(id: UUID = UUID.randomUUID(), name: String, commutes: Vector[Commute])

object Zone {
  implicit val encoder: Encoder[Zone] = deriveEncoder[Zone]
  implicit val decoder: Decoder[Zone] = deriveDecoder[Zone]
}

