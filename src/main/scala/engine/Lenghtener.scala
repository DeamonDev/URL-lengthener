package engine

import zio.*
import domain.*
import scala.collection.mutable.*

trait Lenghtener:
  def dummyZIO(): ZIO[Any, ErrorResponse, Link]

object Lenghtener:

  def apply() =
    for {
      r <- Ref.make(Map[Link, Link]())
    } yield new Lenghtener {
      override def dummyZIO(): ZIO[Any, ErrorResponse, Link] =
        ZIO.succeed(Link("Hello, from ZIO!"))
    }
