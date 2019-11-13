//package com.nbgdev.scheduler.repository
//
//import cats.effect.IO
//import com.nbgdev.scheduler.Util.ErrorOr.ErrorOr
//import com.nbgdev.scheduler.model.ZoneModel.ZoneId
//import com.nbgdev.scheduler.model.{Commute, ZoneModel, Zone}
//import doobie.implicits._
//import doobie.util.transactor.Transactor.Aux
//
//
//trait ZoneRepo {
//  def addZone(zone: Zone): IO[Zone]
//  def getZone(id: ZoneId): IO[Option[Zone]]
//  def deleteZone(id: String): IO[ErrorOr[Unit]]
//  def updatedZone(id: String, zone: Zone): IO[ErrorOr[Zone]]
//  def getZones: IO[Vector[Zone]]
//}
//
//object ZoneRepo {
//  class ZoneRepoImp(xa: Aux[IO, Unit]) extends ZoneRepo {
//    override def addZone(zone: Zone): IO[Zone] = {
//      sql"insert into zones (id, name) values (${zone.name})"
//        .update
//        .run
//        .transact(xa)
//        .map(_ => zone)
//    }
//
//    override def getZone(id: ZoneId): IO[Option[Zone]] = {
//      sql"select zones.id, name, end_zone_name, duration from zones inner join commutes on zones.id = commutes.init_zone".query[Zone].to[Vector]
//      ???
//    }
//
//    override def deleteZone(id: String): IO[ErrorOr[Unit]] = ???
//
//    override def updatedZone(id: String, zone: Zone): IO[ErrorOr[Zone]] = ???
//
//    override def getZones: IO[Vector[Zone]] = ???
//  }
//}
