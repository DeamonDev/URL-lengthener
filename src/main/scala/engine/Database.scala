package engine

import zio.*
import domain.Link
import scala.collection.mutable
import domain.ErrorResponse

trait Database:
  def getAllLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]]
  def getLink(link: Link): ZIO[Any, ErrorResponse, Link]
  def insert(link: Link): ZIO[Any, ErrorResponse, Link]

object Database:
  def getAllLinks(): ZIO[Database, ErrorResponse, List[(Link, Link)]] =
    ZIO.serviceWithZIO(_.getAllLinks())

  def getLink(link: Link): ZIO[Database, ErrorResponse, Link] = 
    ZIO.serviceWithZIO(_.getLink(link))

  def insert(link: Link): ZIO[Database, ErrorResponse, Link] =
    ZIO.serviceWithZIO(_.insert(link))

class DatabaseLive(r: Ref[mutable.Map[Link, Link]]) extends Database:
  override def getAllLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]] =
    for {
      links <- r.get
      _ <-
        if (links.keySet.isEmpty) ZIO.fail(ErrorResponse.EmptyLinkList)
        else ZIO.succeed(())
    } yield links.toList

  override def getLink(link: Link): ZIO[Any, ErrorResponse, Link] = 
    for { 
      links <- r.get
    } yield links(link)

  override def insert(link: Link): ZIO[Any, ErrorResponse, Link] =
    for {
      randomText <- Random.nextUUID
      newLink <- r.modify[Link] { m =>
        val newLink_ = Link(s"localhost:8090/$randomText")
        (newLink_, m + (newLink_ -> link))
      }
    } yield newLink

object DatabaseLive:
  def create(): ZIO[Any, ErrorResponse, Database] =
    for {
      r <- Ref.make(mutable.Map[Link, Link]())
    } yield new DatabaseLive(r)

  def layer: ZLayer[Any, ErrorResponse, Database] = ZLayer.fromZIO(create())


