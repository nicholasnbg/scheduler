package com.nbgdev.scheduler.repository.Zones

import java.util.UUID

import cats.effect.IO
import cats.implicits._
import com.nbgdev.scheduler.model.{Commute, Zone}
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.fragment._
import doobie.util.param.Param
import doobie.util.transactor.Transactor.Aux


trait ZoneRepo {
  def addZone(zone: Zone): IO[Either[String, Zone]]

  def getZone(id: String): IO[Option[Zone]]

  def deleteZone(id: String): IO[Either[String, Unit]]

  //  def updatedZone(id: String, zone: Zone): IO[Either[String, Zone]]
  def getZones: IO[Vector[Zone]]
}

object ZoneRepo {

  class ZoneRepoImp(xa: Aux[IO, Unit]) extends ZoneRepo {
    override def addZone(zone: Zone): IO[Either[String, Zone]] = {
      val validZone: IO[Either[String, Zone]] = for {
        allZones <- getZones
      } yield CommuteTransformer.validCommutes(zone, allZones)

      validZone.flatMap { eoz => {
        eoz.traverse(z => {
          for {
            _ <- insertZone(zone)
            _ <- addCommutes(zone, zone.commutes)
          } yield z
        })
      }}
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

    override def deleteZone(id: String): IO[Either[String, Unit]] = {
      for {
        _ <- deleteCommutes(id)
        deletion <- dz(id)
      } yield deletion
    }

    //    override def updatedZone(id: String, zone: Zone): IO[Either[String, Zone]] = ???

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

    def addCommutes(zone: Zone, commutes: Vector[Commute]): IO[Int] = {
      val body: Fragment = generateStatementBody(commutes, zone)
      val start = sql"insert into commutes (init_zone, end_zone, duration) values"
      val full = start ++ body
      full
        .update
        .run
        .transact(xa)
    }

    private def generateStatementBody(commutes: Vector[Commute], zone: Zone) = {
      val commuteFrags = commutes.map(commute => genCommuteFragment(zone, commute))
      val seperator = Fragment(",", List.empty[Param.Elem])
      val combined = commuteFrags.reduce((acc, next) => acc ++ seperator ++ next)
      combined
    }

    private def genCommuteFragment(zone: Zone, commute: Commute): Fragment = {
      sql"(${zone.id}, ${commute.endZoneId}, ${commute.duration})"
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

    def dz(zoneId: String): IO[Either[String, Unit]] = {
      val uuid = UUID.fromString(zoneId)
      sql"delete from zones where id = ${uuid}"
        .update
        .run
        .map {
          case 0 => Left("No commutes found for that zone id")
          case _ => Right(())
        }.transact(xa)
    }

    def deleteCommutes(zoneId: String): IO[Either[String, Unit]] = {
      val uuid = UUID.fromString(zoneId)
      sql"delete from commutes where end_zone = ${uuid} or init_zone = ${uuid}"
        .update
        .run
        .map {
          case 0 => Left("No commutes found for that zone id")
          case _ => Right(())
        }.transact(xa)
    }
  }

}
