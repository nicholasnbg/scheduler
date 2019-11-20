package com.nbgdev.scheduler.repository

import java.util.UUID

import cats.effect.IO
import com.nbgdev.scheduler.model.Customer
import doobie.util.transactor.Transactor.Aux
import doobie.implicits._
import doobie.postgres.implicits._



sealed trait CustomerRepo{
  def addCustomer(customer: Customer): IO[Customer]
  def getCustomer(id: String): IO[Option[Customer]]
  def deleteCustomer(id: String): IO[Either[String, Unit]]
}

object CustomerRepo {
  class CustomerRepoImpl(xa: Aux[IO, Unit]) extends CustomerRepo {
    override def addCustomer(customer: Customer): IO[Customer] = {
      sql"insert into customers (id, customer_name) values (${customer.id}, ${customer.name})"
        .update
        .run
        .transact(xa)
        .map(_ => customer)
    }

    override def getCustomer(id: String): IO[Option[Customer]] = {
      val uuid = UUID.fromString(id)
      sql"select id, customer_name from customers where id = ${uuid}".query[Customer]
        .option
        .transact(xa)
    }

    override def deleteCustomer(id: String): IO[Either[String, Unit]] = {
      val uuid = UUID.fromString(id)
      sql"delete from customers where id = ${uuid}"
        .update
        .run
        .map {
          case 0 => Left("Customer not found")
          case _ => Right(())
        }.transact(xa)
    }
  }
}
