package com.nbgdev.scheduler.model

import BookModel.{Author, Id, Title}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import scala.util.Random

object BookModel {
  type Title = String
  type Author = String
  type Id = String
  final case class BookId(value: String = Random.alphanumeric.take(8).foldLeft("")((res, c) => res + c))

  implicit val bookWithIdDecoder: Decoder[BookWithId] = deriveDecoder[BookWithId]
  implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]

  implicit val bookWithIdEncoder: Encoder[BookWithId] = deriveEncoder[BookWithId]
  implicit val bookEncoder: Encoder[Book] = deriveEncoder[Book]
}

case class Book(title: Title, author: Author)
case class BookWithId(id: Id, title: Title, author: Author)



