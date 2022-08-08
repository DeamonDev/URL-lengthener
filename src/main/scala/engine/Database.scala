package engine

import zio.*
import domain.Link
import scala.collection.mutable

trait Database:
  def getAllLinks(): Task[scala.collection.mutable.Map[Link, Link]]
  def insert(link: Link): Task[Link]

object Database:
  def getAllLinks()
      : ZIO[Database, Throwable, mutable.Map[Link, Link]] =
    ZIO.serviceWithZIO(_.getAllLinks())

  def insert(link: Link): ZIO[Database, Throwable, Link] = 
    ZIO.serviceWithZIO(_.insert(link))

class DatabaseLive(r: Ref[mutable.Map[Link, Link]]) extends Database:
  override def getAllLinks(): Task[mutable.Map[Link, Link]] = ???
  override def insert(link: Link): Task[Link] = ???

object DatabaseLive:
  def create() =
    for {
      r <- Ref.make(mutable.Map[Link, Link]())
    } yield new DatabaseLive(r)

  def layer: ZLayer[Any, Nothing, Database] = ZLayer.fromZIO(create())
