case class Pet(species: String, url: String)

object Pet {
  given petDecoder: zio.json.JsonDecoder[Pet] =
    zio.json.DeriveJsonDecoder.gen[Pet]
  given petEncoder: zio.json.JsonEncoder[Pet] =
    zio.json.DeriveJsonEncoder.gen[Pet]
}

case class PetError(msg: String)

object PetError {
  given petDecoder: zio.json.JsonDecoder[PetError] =
    zio.json.DeriveJsonDecoder.gen[PetError]
  given petEncoder: zio.json.JsonEncoder[PetError] =
    zio.json.DeriveJsonEncoder.gen[PetError]
}
