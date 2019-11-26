package com.nbgdev.scheduler.model
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.util.UUID

case class Location(id: UUID, name: String, zoneId: UUID)

object Location {
  implicit val encoder: Encoder[Location] = deriveEncoder[Location]
  implicit val decoder: Decoder[Location] = deriveDecoder[Location]
}
