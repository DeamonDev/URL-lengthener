package domain

enum ErrorResponse(msg: String):
  case EmptyLinkList extends ErrorResponse("No Links yet")
  case AlreadyInUse(link: String)
      extends ErrorResponse(s"The link $link is already in use.")

object ErrorResponse:
  given linkDecoder: zio.json.JsonDecoder[ErrorResponse] =
    zio.json.DeriveJsonDecoder.gen[ErrorResponse]
  given linkEncoder: zio.json.JsonEncoder[ErrorResponse] =
    zio.json.DeriveJsonEncoder.gen[ErrorResponse]
