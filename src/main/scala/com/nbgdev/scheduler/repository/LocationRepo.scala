package com.nbgdev.scheduler.repository

import java.util.UUID

import cats.effect.IO
import com.nbgdev.scheduler.model.{Location}
import doobie.util.transactor.Transactor.Aux
import doobie.implicits._
import doobie.postgres.implicits._



trait LocationRepo {
  def addLocation(location: Location): IO[Location]
  def getLocation(id: String): IO[Option[Location]]
  def getLocations: IO[Vector[Location]]
  def deleteLocation(id: String): IO[Either[String, Unit]]
}

object LocationRepo {
  class LocationRepoImpl(xa: Aux[IO, Unit]) extends LocationRepo {
    override def addLocation(location: Location): IO[Location] = {
      sql"insert into locations (id, name, zone_id) values (${location.id}, ${location.name}, ${location.zoneId}"
        .update
        .run
        .transact(xa)
        .map(_ => location)
    }

    override def getLocation(id: String): IO[Option[Location]] = {
      val uuid = UUID.fromString(id)
      sql"select id, name, zone_id from locations where id = ${uuid}"
        .query[Location]
        .option
        .transact(xa)
    }

    override def getLocations: IO[Vector[Location]] = {
      sql"select id, name, zone_id from locations"
        .query[Location]
        .to[Vector]
        .transact(xa)
    }

    override def deleteLocation(id: String): IO[Either[String, Unit]] = {
      val uuid = UUID.fromString(id)
      sql"delete from vehicles where id = ${uuid}"
        .update
        .run
        .map{
          case 0 => Left("Location not found")
          case 1 => Right(())
        }.transact(xa)
    }
  }
}
