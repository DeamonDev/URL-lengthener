package domain

import zio.json.*

enum ErrorResponse(msg: String):
  case EmptyLinkList extends ErrorResponse("No Links yet")
  case AlreadyInUse(link: String)
      extends ErrorResponse(s"The link $link is already in use.")

object ErrorResponse:
  given linkDecoder: JsonDecoder[ErrorResponse] =
    DeriveJsonDecoder.gen[ErrorResponse]
  given linkEncoder: JsonEncoder[ErrorResponse] =
    DeriveJsonEncoder.gen[ErrorResponse]
