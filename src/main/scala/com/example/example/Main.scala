package com.example.example

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.example.example.http.BookRoutes
import com.example.example.repository.BookRepo.DoobieImpl
import com.example.example.repository.{BookRepo, Doobie}
import org.flywaydb.core.Flyway
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  private val bookRepo: BookRepo = new DoobieImpl(Doobie.xa)

  val httpRoutes = Router[IO](
    "/" -> BookRoutes.routes(bookRepo)
  ).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {

    val flyway = Flyway.configure().dataSource("jdbc:postgresql:example", "postgres", "testpass").load()
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
