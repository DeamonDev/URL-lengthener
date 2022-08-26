package engine

import zio.*
import domain.*
import scala.collection.mutable.*

// (1)
trait Lengthener:
  def getLinks: IO[ErrorResponse, List[(Link, Link)]]
  def shortenLink(link: String): IO[ErrorResponse, Link]
  def redirect(link: String): IO[ErrorResponse, String]

// (2)
class LengthenerLive(db: Database, randomLinkGenerator: RandomLinkGenerator) extends Lengthener:
  override val getLinks: IO[ErrorResponse, List[(Link, Link)]] =
    db.getAllLinks
  override def shortenLink(link: String): IO[ErrorResponse, Link] = 
    randomLinkGenerator.getRandomLink.flatMap(newLink => db.insert(Link(link.trim()), newLink))
  override def redirect(link: String): IO[ErrorResponse, String] = 
    db.getLink(Link(s"localhost:8090/$link")).map(_.value)

object LengthenerLive:
  def create(db: Database, randomLinkGenerator: RandomLinkGenerator): LengthenerLive = LengthenerLive(db, randomLinkGenerator)

  // 
  def layer: ZLayer[Database & RandomLinkGenerator, ErrorResponse, Lengthener] =
    ZLayer.fromFunction(create)