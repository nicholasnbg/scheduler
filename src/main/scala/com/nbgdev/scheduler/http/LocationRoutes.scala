package com.nbgdev.scheduler.http

import cats.effect.IO
import com.nbgdev.scheduler.model.{Customer, Location}
import com.nbgdev.scheduler.repository.{CustomerRepo, LocationRepo}
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec._


object LocationRoutes {

  private def errorBody(message: String) = Json.obj(
    ("message", Json.fromString(message))
  )

  def routes(repo: LocationRepo): HttpRoutes[IO] = {

    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {

      case req @ POST -> Root / "location" => {
        req.decode[Location] { location =>
          repo.addLocation(location)
            .flatMap(l => Created(Json.fromString(l.id.toString)))
        }
      }

      case req @ GET -> Root / "location" => {
        repo.getLocations.flatMap(locations => Ok(locations))
      }

      case req @ GET -> Root / "location" / id => {
        repo.getLocation(id).flatMap(Ok(_))
      }

      case req @ DELETE -> Root / "location" / id => {
        repo.deleteLocation(id).flatMap {
          case Left(message) => NotFound(message)
          case Right(_) => Ok()
        }
      }
    }
  }
}
