package com.nbgdev.scheduler.model
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.util.UUID

case class Customer(id: UUID = UUID.randomUUID(), name: String)

object Customer {

  implicit val encoder: Encoder[Customer] = deriveEncoder[Customer]
  implicit val decoder: Decoder[Customer] = deriveDecoder[Customer]
}
