package engine

import zio.*
import domain.Link
import scala.collection.immutable
import domain.ErrorResponse

trait Database:
  def getAllLinks: IO[ErrorResponse, List[(Link, Link)]]
  def getLink(link: Link): IO[ErrorResponse, Link]
  def insert(link: Link, newLink: Link): IO[ErrorResponse, Link]

class DatabaseLive(r: Ref[immutable.Map[Link, Link]]) extends Database:
  override val getAllLinks: IO[ErrorResponse, List[(Link, Link)]] =
    for {
      links <- r.get
      _ <-
        if (links.keySet.isEmpty) ZIO.fail(ErrorResponse.EmptyLinkList)
        else ZIO.unit
    } yield links.toList

  override def getLink(link: Link): IO[ErrorResponse, Link] =
    for {
      links <- r.get
      newLink <-
        if (links.keySet.contains(link)) ZIO.succeed(links(link))
        else ZIO.fail(ErrorResponse.LinkDoesNotExist(link.value))
    } yield newLink

  /*   override def insert(link: Link, newLink: Link): IO[ErrorResponse, Link] =
    for {
      _ <- r.modify[Unit] { m =>
        ((), m + (newLink -> link))
      }
    } yield newLink */

  override def insert(link: Link, newLink: Link): IO[ErrorResponse, Link] =
    r.update { m =>
      m + (newLink -> link)
    }.as(newLink)

object DatabaseLive:
  def create(map: immutable.Map[Link, Link]): ZIO[Any, ErrorResponse, Database] =
    for {
      r <- Ref.make(map)
    } yield new DatabaseLive(r)

  def layer: ZLayer[Any, ErrorResponse, Database] = ZLayer.fromZIO(create(immutable.Map[Link, Link]()))
