package com.nbgdev.scheduler.repository.Zones

import com.nbgdev.scheduler.Util.ErrorOr.ErrorOr
import com.nbgdev.scheduler.model.{Commute, Zone}
import cats.implicits._
import cats.data.Validated._
import cats.data.ValidatedNel

object CommuteTransformer {
  def validateCommutes(newZone: Zone, existingZones: Vector[Zone]): ErrorOr[Zone] = {
    existingZones.map(hasCommute(newZone.commutes, _)).sequence match {
      case Valid(_) => Right(newZone)
      case Invalid(e) => Left(e.reduce)
    }
  }

  private def hasCommute(commutes: Vector[Commute], zone: Zone): ValidatedNel[String, Zone] = {
    if (commutes.map(commute => commute.endZoneId).contains(zone.id)) {
      zone.validNel
    } else
      s"No commute found for ${zone.name}. ".invalidNel
  }
}
