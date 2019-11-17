package com.nbgdev.scheduler.repository.Zones

import java.util.UUID

import cats.effect.IO
import cats.implicits._
import com.nbgdev.scheduler.Util.ErrorOr.ErrorOr
import com.nbgdev.scheduler.model.{Commute, Zone}
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor.Aux


trait ZoneRepo {
  def addZone(zone: Zone): IO[ErrorOr[Zone]]
  def getZone(id: String): IO[Option[Zone]]
  def deleteZone(id: String): IO[ErrorOr[Unit]]
//  def updatedZone(id: String, zone: Zone): IO[ErrorOr[Zone]]
  def getZones: IO[Vector[Zone]]
}

object ZoneRepo {
  class ZoneRepoImp(xa: Aux[IO, Unit]) extends ZoneRepo {
    override def addZone(zone: Zone): IO[ErrorOr[Zone]] = {
      (for {
        allZones <- getZones
        validZone = CommuteTransformer.validateCommutes(zone, allZones)
        addedZone = validZone.traverse(z => insertZone(z))
      } yield addedZone).flatten
    }

    def insertZone(zone: Zone): IO[Zone] = {
      sql"insert into zones (id, name) values (${zone.id},${zone.name})"
        .update
        .run
        .transact(xa)
        .map(_ => zone)
    }

    override def getZone(id: String): IO[Option[Zone]] = {
      for {
        maybeName <- getZoneName(id)
        commutes <- getCommutes(id)
        zone = maybeName.map(name => Zone(UUID.fromString(id), name, commutes))
      } yield zone
    }

    override def deleteZone(id: String): IO[ErrorOr[Unit]] = {
      for {
        _ <- deleteCommutes(id)
        deletion <- dz(id)
      } yield deletion
    }

//    override def updatedZone(id: String, zone: Zone): IO[ErrorOr[Zone]] = ???

    override def getZones: IO[Vector[Zone]] = {
      for {
        zoneIds <- getZoneIds
        maybeZones <- zoneIds.traverse(id => getZone(id))
        zones = maybeZones.flatten
      } yield zones
    }

    def getCommutes(zoneId: String): IO[Vector[Commute]] = {
      val uuid = UUID.fromString(zoneId)
       sql"select end_zone, duration from commutes left join zones on zones.id = commutes.init_zone where zones.id = ${uuid}"
        .query[Commute]
        .to[Vector]
        .transact(xa)
    }

    def addCommutes(zone: Zone, commutes: Vector[Commute]) = {

    }

    def getZoneName(zoneId: String): IO[Option[String]] = {
      val uuid = UUID.fromString(zoneId)
      sql"select name from zones where id = ${uuid}"
        .query[String]
        .option
        .transact(xa)
    }

    def getZoneIds: IO[Vector[String]] = {
      sql"select id from zones"
        .query[UUID]
        .to[Vector]
        .transact(xa)
        .map(v => v.map(uuid => uuid.toString))

    }

    def dz(zoneId: String): IO[ErrorOr[Unit]] = {
      val uuid = UUID.fromString(zoneId)
      sql"delete from zones where id = ${uuid}"
        .update
        .run
        .map {
          case 0 => Left("No commutes found for that zone id")
          case _ => Right(())
        }.transact(xa)
    }

    def deleteCommutes(zoneId: String):IO[ErrorOr[Unit]] = {
      val uuid = UUID.fromString(zoneId)
      sql"delete from commutes where init_zone = ${uuid} or end_zone = ${uuid}"
        .update
        .run
        .map {
          case 0 => Left("No commutes found for that zone id")
          case _ => Right(())
        }.transact(xa)
    }
  }
}
