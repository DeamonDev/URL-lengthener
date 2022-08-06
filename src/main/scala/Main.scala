import zhttp.http.*
import zhttp.service.Server
import zio.*

import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.ziohttp.*
import sttp.tapir.generic.auto.*

import Pet.*

object Main extends ZIOAppDefault:

  val petEndpoint: PublicEndpoint[Int, PetError, Pet, Any] = 
    endpoint.get.in("pet" / path[Int]("petId")).errorOut(jsonBody[PetError]).out(jsonBody[Pet])

  val app  = 
    ZioHttpInterpreter().toHttp(
      petEndpoint.zServerLogic(petId => 
        if (petId == 35) ZIO.succeed(Pet("X", "localhost/x/y"))
        else ZIO.fail(PetError("not known pet")))
    )

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    Server.start(8090, app).exitCode
