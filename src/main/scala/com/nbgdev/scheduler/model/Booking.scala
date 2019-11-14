package com.nbgdev.scheduler.model

import java.time.LocalDate
import java.util.UUID

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

