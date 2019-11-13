package com.nbgdev.scheduler.http

import cats.effect.IO
import com.nbgdev.scheduler.model.Vehicle
import com.nbgdev.scheduler.repository.VehicleRepo
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec._


object VehicleRoutes {

    private def errorBody(message: String) = Json.obj(
      ("message", Json.fromString(message))
    )

    def routes(repo: VehicleRepo): HttpRoutes[IO] = {

      val dsl = new Http4sDsl[IO] {}
      import dsl._

      HttpRoutes.of[IO] {
        case req @ GET -> Root / "vehicles" => {
          repo.getVehicles.flatMap(Ok(_))
        }

        case req @ POST -> Root / "vehicles" => {
          req.decode[Vehicle] { vehicle =>
            repo.addVehicle(vehicle)
              .flatMap(v => Created(Json.fromString(v.rego)))
          }
        }

        case req @ GET -> Root / "vehicles" / rego => {
          repo.getVehicle(rego).flatMap(Ok(_))
        }

        case req @ DELETE -> Root / "vehicles" / rego => {
          repo.deleteVehicle(rego).flatMap {
            case Left(message) => NotFound(message)
            case Right(_) => Ok()
          }
        }
      }
    }
}
