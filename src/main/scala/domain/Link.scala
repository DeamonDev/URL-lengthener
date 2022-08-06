package domain

case class Link(value: String)

object Link: 
  given linkDecoder: zio.json.JsonDecoder[Link] = zio.json.DeriveJsonDecoder.gen[Link]
  given linkEncoder: zio.json.JsonEncoder[Link] = zio.json.DeriveJsonEncoder.gen[Link]