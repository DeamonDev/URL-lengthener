package engine

import zio.*
import domain.*
import scala.collection.mutable.*

trait Lengthener:
  def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]]
  def shortenLink(link: String): ZIO[Any, ErrorResponse, Link]
  def redirect(link: String): ZIO[Any, Unit, String]

object Lengthener:

  def apply() =
    for {
      r <- Ref.make(Map[Link, Link]())
    } yield new Lengthener {

      override def getLinks(): ZIO[Any, ErrorResponse, List[(Link, Link)]] =
        for {
          links <- r.get
          _ <-
            if (links.keySet.isEmpty) ZIO.fail(ErrorResponse.EmptyLinkList)
            else ZIO.succeed(())
        } yield links.toList

      override def shortenLink(link: String): ZIO[Any, ErrorResponse, Link] =
        for {
          randomText <- Random.nextUUID
          newLink <- r.modify[Link] { m =>
            val newLink_ = Link(s"localhost:8090/$randomText")
            (newLink_, m + (newLink_ -> Link(s"$link")))
          }
        } yield newLink

      override def redirect(link: String): ZIO[Any, Unit, String] = ZIO.succeed("google.com")
    }
