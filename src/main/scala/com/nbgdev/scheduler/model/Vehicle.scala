package com.nbgdev.scheduler.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}



case class Vehicle(rego: String, capacity: Int)

object Vehicle {

  implicit val encoder: Encoder[Vehicle] = deriveEncoder[Vehicle]
  implicit val decoder: Decoder[Vehicle] = deriveDecoder[Vehicle]
}
