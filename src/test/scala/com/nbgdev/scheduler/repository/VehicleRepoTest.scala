//package com.nbgdev.scheduler.repository
//
//import cats.effect.IO
//import com.nbgdev.scheduler.repository.VehicleRepo.VehicleRepoImpl
//import doobie.util.transactor.Transactor
//import doobie._
//import doobie.implicits._
//import doobie.util.transactor.Transactor._
//import org.specs2.mutable.Specification
//
//object VehicleRepoTest extends Specification with doobie.specs2.IOChecker {
//
//  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
//
//    val transactor = Transactor.fromDriverManager[IO](
//      "org.postgresql.Driver", "jdbc:postgresql:world", "postgres", ""
//    )
//
//  val repo = new VehicleRepoImpl(transactor)
//
//  check(repo.testQuery)
//}
