import zhttp.http.*
import zhttp.service.Server
import zio.*

import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.*
import sttp.tapir.generic.auto.*

import domain.*
import engine.Lenghtener


object Main extends ZIOAppDefault:

  val urlsEndpoint = 
    endpoint.get.in("urls").errorOut(stringBody).out(jsonBody[Link])

  val app  = 
    ZioHttpInterpreter().toHttp(
      urlsEndpoint.zServerLogic(_ => Lenghtener.dummyZIO())
    )

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    Server.start(8090, app).exitCode
