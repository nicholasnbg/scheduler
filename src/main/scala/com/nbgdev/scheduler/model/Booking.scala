package com.nbgdev.scheduler.model

import java.time.LocalDate
import java.util.UUID

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import org.joda.time.LocalTime



case class Booking(
                     id: UUID,
                     customer: Customer,
                     passengers: Int,
                     pickup: Location,
                     dropoff: Location,
                     duration: Int,
                     date: LocalDate,
                     start_time: LocalTime,
                     vehicle: Vehicle)

case class BookingWithIds(
                           id: UUID,
                           customer: UUID,
                           passengers: Int,
                           pickup: UUID,
                           dropoff: UUID,
                           duration: Int,
                           date: LocalDate,
                           start_time: LocalTime,
                           vehicle: UUID)

object BookingWithIds {
  implicit val encoder: Encoder[BookingWithIds] = deriveEncoder[BookingWithIds]
  implicit val decoder: Decoder[BookingWithIds] = deriveDecoder[BookingWithIds]
}
