package com.nbgdev.scheduler.repository

import cats.effect.IO
import com.nbgdev.scheduler.Util.ErrorOr.ErrorOr
import com.nbgdev.scheduler.model.Vehicle
import doobie.util.transactor.Transactor.Aux
import doobie.implicits._


sealed trait VehicleRepo{
def addVehicle(Vehicle: Vehicle): IO[Vehicle]
def getVehicle(rego: String): IO[Option[Vehicle]]
def deleteVehicle(rego: String): IO[ErrorOr[Unit]]
def getVehicles: IO[Vector[Vehicle]]
}

object VehicleRepo {
  class VehicleRepoImpl(xa: Aux[IO, Unit]) extends VehicleRepo {
    override def addVehicle(vehicle: Vehicle): IO[Vehicle] = {
      sql"insert into vehicles (rego, capacity) values (${vehicle.rego}, ${vehicle.capacity})"
        .update
        .run
        .transact(xa)
        .map(_ => vehicle)
    }

    override def getVehicle(rego: String): IO[Option[Vehicle]] = {
      sql"select rego, capacity from vehicles where rego = ${rego}".query[Vehicle]
        .option
        .transact(xa)
    }

    override def deleteVehicle(rego: String): IO[ErrorOr[Unit]] = {
      sql"delete from vehicles where rego = ${rego}"
        .update
        .run
        .map {
          case 0 => Left("Vehicle not found")
          case _ => Right(())
        }.transact(xa)
    }

    override def getVehicles: IO[Vector[Vehicle]] = {
      sql"select rego, capacity from vehicle".query[Vehicle]
        .to[Vector]
        .transact(xa)
    }
  }
}
