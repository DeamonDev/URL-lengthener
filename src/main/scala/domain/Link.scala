package domain

case class Link(value: String)

object Link: 
  given linkDecoder: zio.json.JsonDecoder[Link] = zio.json.DeriveJsonDecoder.gen[Link]
  given linkEncoder: zio.json.JsonEncoder[Link] = zio.json.DeriveJsonEncoder.gen[Link]

  given mapLinkDecoder: zio.json.JsonDecoder[List[(Link, Link)]] = zio.json.DeriveJsonDecoder.gen[List[(Link, Link)]]
  given mapLinkEncoder: zio.json.JsonEncoder[List[(Link, Link)]] = zio.json.DeriveJsonEncoder.gen[List[(Link, Link)]]