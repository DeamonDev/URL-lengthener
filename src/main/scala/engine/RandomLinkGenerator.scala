package engine

import zio.*
import domain.Link

trait RandomLinkGenerator:
  def getRandomLink: UIO[Link]

final class RandomLinkGeneratorLive() extends RandomLinkGenerator: 
  override val getRandomLink: UIO[Link] = 
    Random.nextUUID.map(uuid => Link(s"localhost:8090/$uuid"))

object RandomLinkGeneratorLive:
  def layer: ZLayer[Any, Nothing, RandomLinkGenerator] = 
    ZLayer.succeed(RandomLinkGeneratorLive())