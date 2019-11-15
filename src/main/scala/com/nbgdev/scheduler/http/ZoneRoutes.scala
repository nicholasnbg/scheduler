//package com.nbgdev.scheduler.http
//
//import cats.effect.IO
//import com.nbgdev.scheduler.repository.ZoneRepo
//import io.circe.Json
//import org.http4s.HttpRoutes
//import org.http4s.dsl.Http4sDsl
//
//object ZoneRoutes {
//
//  private def errorBody(message: String) = Json.obj(
//    ("message", Json.fromString(message))
//  )
//
//  def routes(zoneRepo: ZoneRepo): HttpRoutes[IO] = {
//
//    val dsl = new Http4sDsl[IO] {}
//    import dsl._
//
//    HttpRoutes.of[IO] {
//      case req @ GET -> Root / "zones" => {
//        ???
//      }
//
//      case req @ POST -> Root / "zones" => {
//        ???
//      }
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
//    }
//  }
//}
