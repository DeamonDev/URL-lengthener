val scala3Version = "3.1.3"

lazy val zioVersion = "2.0.0"
lazy val ZHTTPVersion = "2.0.0-RC10"
lazy val tapirVersion = "1.0.3"

val zio = Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-json" % "0.3.0-RC10",
  "io.d11" %% "zhttp" % ZHTTPVersion,
  "dev.zio" %% "zio-logging-slf4j" % "2.0.1"
)

val tapir = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "link-longhtener",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= zio ++ tapir
  )
