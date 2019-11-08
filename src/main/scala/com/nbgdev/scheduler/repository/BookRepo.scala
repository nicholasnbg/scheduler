package com.nbgdev.scheduler.repository

import cats.effect.IO
import com.nbgdev.scheduler.model.{Book, BookWithId}
import com.nbgdev.scheduler.model.BookModel.BookId
import doobie.implicits._
import doobie.util.transactor.Transactor.Aux

trait BookRepo {

  def addBook(book: Book): IO[BookId]
  def getBook(id: BookId): IO[Option[BookWithId]]
  def deleteBook(id: BookId): IO[Either[String, Unit]]
  def updateBook(id: BookId, book: Book): IO[Either[String, Unit]]
  def getBooks: IO[Vector[BookWithId]]

}

object BookRepo {

  class HashImpl extends BookRepo {

    import scala.collection.mutable.HashMap
    val storage = HashMap[BookId, Book]().empty

    override def addBook(book: Book): IO[BookId] = IO {
      val bookId = BookId()
      storage.put(bookId, book)
      bookId
    }

    override def getBook(id: BookId): IO[Option[BookWithId]] = IO {
      val book = storage.get(id)
      book.map(book => BookWithId(id.value, book.title, book.author))
    }

    override def deleteBook(id: BookId): IO[Either[String, Unit]] =
      for {
        maybeRemovedBook <- IO(storage.remove(id))
        result = maybeRemovedBook.toRight("Book not found").map(_ => ())
      } yield result

    override def updateBook(id: BookId, book: Book): IO[Either[String, Unit]] =
      for {
        maybeBook <- getBook(id)
        _ <- IO(maybeBook.toRight("book not found").map(_ => ()))
        updatedBook = storage.put(id, book).toRight("book not found").map(_ => ())
      } yield updatedBook

    override def getBooks: IO[Vector[BookWithId]] = IO {
      storage.map{case (id,book) => BookWithId(id.value, book.title, book.author)}.toVector
    }
  }

  class DoobieImpl(xa: Aux[IO, Unit]) extends BookRepo {
    override def addBook(book: Book): IO[BookId] = {
      val bookId = BookId()
      sql"insert into book (id, title, author) values (${bookId.value} ,${book.title}, ${book.author})"
        .update
        .run
        .transact(xa)
        .map(_ => bookId)
    }

    override def getBook(id: BookId): IO[Option[BookWithId]] = {
      sql"select id, title, author from book where id = ${id.value}".query[BookWithId]
        .option
        .transact(xa)
    }

    override def deleteBook(id: BookId): IO[Either[String, Unit]] = {
      sql"delete from book where id = ${id.value}".update.run.map {
        case 0 => Left("Book not found")
        case _ => Right(())
      }.transact(xa)
    }

    override def updateBook(id: BookId, book: Book): IO[Either[String, Unit]] = {
      sql"update book set title = ${book.title}, author = ${book.author} where id = ${id.value}"
        .update.run.map{
        case 0 => Left("Book not found")
        case _ => Right(())
      }.transact(xa)
    }

    override def getBooks: IO[Vector[BookWithId]] = {
      sql"select id, title, author from book".query[BookWithId].to[Vector].transact(xa)
    }
  }
}
