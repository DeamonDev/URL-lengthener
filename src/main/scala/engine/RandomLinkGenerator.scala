package engine

import zio.*
import domain.Link


trait RandomLinkGenerator:
  def getRandomLink(): UIO[Link]

class RandomLinkGeneratorLive() extends RandomLinkGenerator: 
  override def getRandomLink(): UIO[Link] = 
    Random.nextUUID.map(uuid => Link(s"localhost:8090/$uuid"))

object RandomLinkGeneratorLive:
  def create(): RandomLinkGeneratorLive = RandomLinkGeneratorLive()

  def layer: ZLayer[Any, Nothing, RandomLinkGenerator] = 
    ZLayer.succeed(create())