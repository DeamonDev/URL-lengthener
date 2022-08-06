package engine

import zio.* 
import domain.*

object Lenghtener: 
  def dummyZIO() = ZIO.succeed(Link("simple link"))