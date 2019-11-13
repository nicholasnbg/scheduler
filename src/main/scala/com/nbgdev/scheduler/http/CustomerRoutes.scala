package com.nbgdev.scheduler.http

import cats.effect.IO
import com.nbgdev.scheduler.model.Customer
import com.nbgdev.scheduler.repository.CustomerRepo
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec._

object CustomerRoutes {

  private def errorBody(message: String) = Json.obj(
    ("message", Json.fromString(message))
  )

  def routes(repo: CustomerRepo): HttpRoutes[IO] = {

    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {

      case req @ POST -> Root / "customer" => {
        req.decode[Customer] { customer =>
          repo.addCustomer(customer)
            .flatMap(c => Created(Json.fromString(c.id.toString)))
        }
      }

      case req @ GET -> Root / "customer" / id => {
        repo.getCustomer(id).flatMap(Ok(_))
      }

      case req @ DELETE -> Root / "customer" / id => {
        repo.deleteCustomer(id).flatMap {
          case Left(message) => NotFound(message)
          case Right(_) => Ok()
        }
      }
    }
  }

}
