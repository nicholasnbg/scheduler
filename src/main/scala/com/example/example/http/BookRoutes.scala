package com.example.example.http

import cats.effect.IO
import com.example.example.model.Book
import com.example.example.model.BookModel._
import com.example.example.repository.BookRepo
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl


object BookRoutes {

  private def errorBody(message: String) = Json.obj(
    ("message", Json.fromString(message))
  )

  def routes(bookRepo: BookRepo): HttpRoutes[IO] = {

    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case req @ GET -> Root / "books" => {
        bookRepo.getBooks.flatMap(books => Ok(books))
      }

      case req @ POST -> Root / "books" => {
        req.decode[Book] { book =>
          bookRepo.addBook(book).flatMap(id =>
            Created(Json.obj(("id", Json.fromString(id.value))))
          )
        }
      }

      case req @ GET -> Root / "books" / id => {
        bookRepo.getBook(BookId(id)).flatMap {
          case Some(book) => Ok(book)
          case None => NotFound()
        }
      }

      case req @ PUT -> Root / "books" / id =>
        req.decode[Book] { book =>
          bookRepo.updateBook(BookId(id), book) flatMap {
            case Left(message) => NotFound(errorBody(message))
            case Right(_) => Ok()
          }
        }

      case _ @ DELETE -> Root / "books" / id =>
        bookRepo.deleteBook(BookId(id)) flatMap {
          case Left(message) => NotFound(errorBody(message))
          case Right(_) => Ok()
        }
    }
  }


}
