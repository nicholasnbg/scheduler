
val Http4sVersion = "0.20.8"
val CirceVersion = "0.11.1"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"

enablePlugins(JavaAppPackaging, AshScriptPlugin)

dockerBaseImage := "openjdk:8-jre-alpine"
version in Docker := "0.1.1"

lazy val root = (project in file("."))
  .settings(
    organization := "com.nbgdev",
    name := "scheduler",
    version := "0.0.1",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.tpolecat"    %% "doobie-core"         % "0.8.4",
      "org.tpolecat"    %% "doobie-postgres"     % "0.8.4",
      "org.tpolecat"    %% "doobie-h2"           % "0.8.4",
      "org.tpolecat"    %% "doobie-hikari"       % "0.8.4",
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.flywaydb"    % "flyway-core"          % "5.2.4",
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
      "org.tpolecat"    %% "doobie-specs2"       % "0.8.4"       % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
