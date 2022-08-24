package engine

import zio.*
import domain.*
import scala.collection.mutable.*

// (1)
trait Lengthener:
  def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]]
  def shortenLink(link: String): ZIO[Any, ErrorResponse, Link]
  def redirect(link: String): ZIO[Any, ErrorResponse, String]

// (2)
class LengthenerLive(db: Database, randomLinkGenerator: RandomLinkGenerator) extends Lengthener:
  override def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]] =
    db.getAllLinks()
  override def shortenLink(link: String): ZIO[Any, ErrorResponse, Link] = 
    randomLinkGenerator.getRandomLink.flatMap(newLink => db.insert(Link(link.trim()), newLink))
  override def redirect(link: String): ZIO[Any, ErrorResponse, String] = 
    db.getLink(Link(s"localhost:8090/$link")).map(_.value)

object LengthenerLive:
  def create(db: Database, randomLinkGenerator: RandomLinkGenerator): LengthenerLive = LengthenerLive(db, randomLinkGenerator)

  // 
  def layer: ZLayer[Database & RandomLinkGenerator, ErrorResponse, Lengthener] =
    ZLayer.fromFunction(create)