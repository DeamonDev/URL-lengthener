import zhttp.http.*
import zhttp.service.Server
import zio.*

import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.*
import sttp.tapir.generic.auto.*
import sttp.model.StatusCode

import sttp.tapir.swagger.bundle.SwaggerInterpreter

import domain.*
import engine.Lengthener
import sttp.tapir.server.ServerEndpoint
import java.util.UUID
import sttp.tapir.model.StatusCodeRange.Redirect

object Main extends ZIOAppDefault:

  val urlsEndpoint =
    endpoint.get
      .in("urls")
      .errorOut(jsonBody[ErrorResponse])
      .out(jsonBody[List[(Link, Link)]])

  val postLinkEndpoint =
    endpoint.post
      .in("url")
      .in(stringBody)
      .errorOut(jsonBody[ErrorResponse])
      .out(jsonBody[Link])

  def redirectEndpointRetriever = 
    endpoint.get
    .in(path[String]("uuid"))
    .out(header[String](sttp.model.HeaderNames.Location))
    .out(statusCode(StatusCode.apply(303)))
    

  val redirect =
    endpoint.get
      .in("red")
      .out(
          header(sttp.model.Header("Location", "https://www.tibia.pl/"))
        )
      .out(statusCode(StatusCode.apply(303)))

  val swaggerEndpoints = SwaggerInterpreter()
    .fromEndpoints[Task](List(urlsEndpoint, postLinkEndpoint), "My App", "1.0")

  def app_v2 = ZioHttpInterpreter().toHttp(swaggerEndpoints)

  def app(lengthener: Lengthener) =
    ZioHttpInterpreter().toHttp(
      List(
        urlsEndpoint.zServerLogic(_ => lengthener.getLinks()),
        postLinkEndpoint.zServerLogic(link => lengthener.shortenLink(link)),
        redirect.zServerLogic(_ => ZIO.succeed(())),
        redirectEndpointRetriever.zServerLogic(s => ZIO.succeed("https://www.google.com"))
      ) ++ swaggerEndpoints
    )

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    for {
      lenghtener <- Lengthener()
      _ <- Server.start(8090, app(lenghtener) @@ Middleware.debug).exitCode
    } yield ()
