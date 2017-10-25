package com.alerf.mutils.dict.marshalling

import io.circe.{ACursor, Decoder, HCursor}
import com.alerf.mutils.dict._
import io.circe.Decoder.Result

import scala.util.Random

object JsonCirce {
  import com.alerf.mutils.marshalling.JsonCirce.eitherDecoder

  private val oldDictionaryDecoder: Decoder[Dict] =
    new Decoder[Dict] {
      private val _dec = implicitly[Decoder[Map[String, Either[String, Vector[String]]]]]
      override def apply(c: HCursor): Result[Dict] = {
        implicit val rGen = Random
        _dec(c).map { rawDict =>
          Dict(rawDict.mapValues {
            case Left(str) => Single(str)
            case Right(strs) => Multi(strs)
          })
        }
      }
    }

  implicit val dictDecoder: Decoder[Dict] =
    new Decoder[Dict] {

      implicit private val rGen = Random

      private type RVE = Result[Vector[(String, Entry)]]
      private lazy val noEntries: RVE = Right(Vector.empty)

      private def traverse(c: ACursor, prefix: Seq[String]): RVE = {
        lazy val key = prefix.mkString(".")
        c.focus.fold(noEntries) { json =>
          if (json.isString)
            json.as[String].map(Single).map(v => Vector(key -> v))
          else if (json.isArray)
            json.as[Vector[String]].map(Multi.apply).map(v => Vector(key -> v))
          else if (json.isObject)
            json.hcursor.fields.fold(noEntries) { fields =>
              fields.map { field =>
                c.downField(field).success.fold(noEntries)(traverse(_, prefix :+ field))
              }.foldLeft(noEntries) { case (acc, res) =>
                  for {
                    accV <- acc
                    resV <- res
                  } yield accV ++ resV
              }
            }
          else
            noEntries
        }
      }

      override def apply(c: HCursor): Result[Dict] =
        traverse(c, Seq.empty).right.map(c => Dict(c.toMap))

    }
}
