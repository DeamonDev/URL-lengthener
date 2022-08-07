import zhttp.http.*
import zhttp.service.Server
import zio.*

import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.*
import sttp.tapir.generic.auto.*

import domain.*
import engine.Lengthener

object Main extends ZIOAppDefault:

  val urlsEndpoint =
    endpoint.get
      .in("urls")
      .errorOut(jsonBody[ErrorResponse])
      .out(jsonBody[List[Link]])

  val postLinkEndpoint =
    endpoint.post
      .in("url")
      .in(stringBody)
      .errorOut(jsonBody[ErrorResponse])
      .out(jsonBody[Link])

  def app(lengthener: Lengthener) =
    ZioHttpInterpreter().toHttp(
      List(
        urlsEndpoint.zServerLogic(_ => lengthener.getLinks()),
        postLinkEndpoint.zServerLogic(link => lengthener.shortenLink(link))
      )
    )

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    for {
      lenghtener <- Lengthener()
      _ <- Server.start(8090, app(lenghtener) @@ Middleware.debug).exitCode
    } yield ()
