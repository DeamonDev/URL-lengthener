package engine

import zio.*
import domain.*
import scala.collection.mutable.*

trait Lengthener:
  def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]]
  def shortenLink(link: String): ZIO[Any, ErrorResponse, Link]
  def redirect(link: String): ZIO[Any, ErrorResponse, String]

class LengthenerLive(db: Database) extends Lengthener:
  override def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]] =
    db.getAllLinks()
  override def shortenLink(link: String): ZIO[Any, ErrorResponse, Link] = 
    db.insert(Link(link.trim()))
  override def redirect(link: String): ZIO[Any, ErrorResponse, String] = 
    ZIO.succeed(println(s"[link] $link")) *> db.getLink(Link(s"localhost:8090/$link")).map(_.value)

object LengthenerLive:
  def create(db: Database): LengthenerLive = LengthenerLive(db)

  def layer: ZLayer[Database, ErrorResponse, Lengthener] =
    ZLayer.fromFunction(create)