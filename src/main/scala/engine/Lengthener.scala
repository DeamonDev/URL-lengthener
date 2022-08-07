package engine

import zio.*
import domain.*
import scala.collection.mutable.*

trait Lengthener:
  def getLinks(): ZIO[Any, ErrorResponse, List[Link]]
  def shortenLink(link: String): ZIO[Any, ErrorResponse, Link]

object Lengthener:

  def apply() =
    for {
      r <- Ref.make(Map[Link, Link]())
    } yield new Lengthener {

      override def getLinks(): ZIO[Any, ErrorResponse, List[Link]] =
        for {
          links <- r.get
          _ <-
            if (links.keySet.isEmpty) ZIO.fail(ErrorResponse.EmptyLinkList)
            else ZIO.succeed(())
        } yield links.values.toList

      override def shortenLink(link: String): ZIO[Any, ErrorResponse, Link] =
        for {
          randomText <- Random.nextUUID
          newLink <- r.modify[Link] { m =>
            val newLink_ = Link(s"localhost:8090/$randomText")
            (newLink_, m + (newLink_ -> Link(s"$link")))
          }
        } yield newLink
    }
