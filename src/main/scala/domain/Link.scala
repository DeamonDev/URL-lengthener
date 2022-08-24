package domain

import zio.json.*

final case class Link(value: String)

object Link:
  given linkDecoder: JsonDecoder[Link] = DeriveJsonDecoder.gen[Link]
  given linkEncoder: JsonEncoder[Link] = DeriveJsonEncoder.gen[Link]

  given mapLinkDecoder: JsonDecoder[List[(Link, Link)]] =
    DeriveJsonDecoder.gen[List[(Link, Link)]]
  given mapLinkEncoder: JsonEncoder[List[(Link, Link)]] =
    DeriveJsonEncoder.gen[List[(Link, Link)]]
