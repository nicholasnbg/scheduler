package com.nbgdev.scheduler.http

import cats.effect.IO
import com.nbgdev.scheduler.model.Zone
import com.nbgdev.scheduler.repository.Zones.ZoneRepo
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec._


object ZoneRoutes {

  private def errorBody(message: String) = Json.obj(
    ("message", Json.fromString(message))
  )

  def routes(zoneRepo: ZoneRepo): HttpRoutes[IO] = {

    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case req @ GET -> Root / "zones" => {
        zoneRepo.getZones.flatMap(Ok(_))
      }

      case req @ POST -> Root / "zones" => {
        req.decode[Zone] { zone =>
          zoneRepo.addZone(zone).flatMap {
            case Left(e) => BadRequest(e)
            case Right(z) => Created(Json.fromString(z.name))
          }
        }
      }
//
//      case req @ GET -> Root / "zones" / zoneId => {
//        ???
//      }
//
//      case req @ PUT -> Root / "zones" / zoneId => {
//        ???
//      }
//
//      case req @ DELETE -> Root / "zones" / zoneId => {
//        ???
//      }
    }
  }
}
