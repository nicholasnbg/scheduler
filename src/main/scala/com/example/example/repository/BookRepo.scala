package com.example.example.repository

import cats.effect.IO
import com.example.example.model.{Book, BookWithId}
import com.example.example.model.BookModel.{BookId}

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
}
