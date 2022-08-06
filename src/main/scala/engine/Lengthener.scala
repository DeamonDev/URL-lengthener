package engine

import zio.*
import domain.*
import scala.collection.mutable.*

trait Lengthener:
  def dummyZIO(): ZIO[Any, ErrorResponse, Link]

object Lengthener:

  def apply() =
    for {
      r <- Ref.make(Map[Link, Link]())
    } yield new Lengthener {
      override def dummyZIO(): ZIO[Any, ErrorResponse, Link] =
        ZIO.succeed(Link("Hello, from ZIO!"))
    }
