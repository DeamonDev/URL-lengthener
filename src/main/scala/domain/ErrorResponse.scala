package domain

enum ErrorResponse(msg: String): 
  case EmptyLinkList extends ErrorResponse("No Links yet")

object ErrorResponse:
  given linkDecoder: zio.json.JsonDecoder[ErrorResponse] = zio.json.DeriveJsonDecoder.gen[ErrorResponse]
  given linkEncoder: zio.json.JsonEncoder[ErrorResponse] = zio.json.DeriveJsonEncoder.gen[ErrorResponse]