package com.nbgdev.scheduler

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.nbgdev.scheduler.http.{CustomerRoutes, LocationRoutes, VehicleRoutes, ZoneRoutes}
import com.nbgdev.scheduler.repository.BookRepo.DoobieImpl
import com.nbgdev.scheduler.repository.CustomerRepo.CustomerRepoImpl
import com.nbgdev.scheduler.repository.LocationRepo.LocationRepoImpl
import com.nbgdev.scheduler.repository.VehicleRepo.VehicleRepoImpl
import com.nbgdev.scheduler.repository.Zones.ZoneRepo.ZoneRepoImp
import com.nbgdev.scheduler.repository.Zones.ZoneRepo
import com.nbgdev.scheduler.repository.{BookRepo, CustomerRepo, Doobie, LocationRepo, VehicleRepo}
import org.flywaydb.core.Flyway
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  private val bookRepo: BookRepo = new DoobieImpl(Doobie.xa)
  private val vehicleRepo: VehicleRepo = new VehicleRepoImpl(Doobie.xa)
  private val customerRepo: CustomerRepo = new CustomerRepoImpl(Doobie.xa)
  private val zoneRepo: ZoneRepo = new ZoneRepoImp(Doobie.xa)
  private val locationRepo: LocationRepo = new LocationRepoImpl(Doobie.xa)

  val httpRoutes = Router[IO](
//    "/" -> BookRoutes.routes(bookRepo),
    "/" -> VehicleRoutes.routes(vehicleRepo),
    "/" -> CustomerRoutes.routes(customerRepo),
    "/" -> ZoneRoutes.routes(zoneRepo),
    "/" -> LocationRoutes.routes(locationRepo)
  ).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {

    val flyway = Flyway.configure().dataSource("jdbc:postgresql:scheduler", "postgres", "testpass").load()
    flyway.migrate()

    BlazeServerBuilder[IO]
      .bindHttp(7777, "0.0.0.0")
      .withHttpApp(httpRoutes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
