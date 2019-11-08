package com.nbgdev.scheduler.model

import org.joda.time.LocalTime


case class Booking(startTime: LocalTime, endTime: LocalTime, origin: Resort, destination: Destination, vehicle: Vehicle)

