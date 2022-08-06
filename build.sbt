val scala3Version = "3.1.3"

lazy val zioVersion = "2.0.0"
lazy val ZHTTPVersion = "2.0.0-RC10"

val zio = Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-json" % "0.3.0-RC10",
  "io.d11" %% "zhttp" % ZHTTPVersion
)

val tapir = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.0.3",
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % "1.0.3",
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % "1.0.3"
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "link-longhtener",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies ++= zio ++ tapir
  )
