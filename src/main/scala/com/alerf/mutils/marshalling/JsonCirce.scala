package com.alerf.mutils.marshalling

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}

object JsonCirce {
  implicit def eitherDecoder[A, B](implicit
                                           aDec: Decoder[A],
                                           bDec: Decoder[B]): Decoder[Either[A, B]] =
    new Decoder[Either[A, B]] {
      override def apply(c: HCursor): Result[Either[A, B]] =
        bDec(c).map(r => Right(r)).left.flatMap { _ =>
          aDec(c).map(l => Left(l))
        }
    }

}
