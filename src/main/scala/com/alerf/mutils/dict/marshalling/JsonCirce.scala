package com.alerf.mutils.dict.marshalling

import scala.util.Random

import io.circe.{ACursor, Decoder, HCursor}
import io.circe.Decoder.Result

import com.alerf.mutils.dict._
import com.alerf.mutils.marshalling.JsonCirce.eitherDecoder

object JsonCirce {


  type RawDict = Map[String, Either[String, Vector[String]]]

  private val oldDictionaryDecoder: Decoder[Dict] =
    new Decoder[Dict] {
      private val _dec = summon[Decoder[RawDict]]
      override def apply(c: HCursor): Result[Dict] = {
        implicit val rGen = Random
        _dec(c).map { (rawDict: RawDict) =>
          Dict(rawDict.view.mapValues {
            case Left(str) => Single(str)
            case Right(strs) => Multi(strs)
          }.toMap)
        }
      }
    }

  implicit val dictDecoder: Decoder[Dict] =
    new Decoder[Dict] {

      private given rGen: Random = Random

      private type RVE = Result[Vector[(String, Entry)]]
      private lazy val noEntries: RVE = Right(Vector.empty)

      private def traverse(c: ACursor, prefix: Seq[String]): RVE = {
        lazy val key = prefix.mkString(".")
        c.focus.fold(noEntries) { json =>
          if (json.isString)
            json.as[String].map(Single.apply).map(v => Vector(key -> v))
          else if (json.isArray)
            json.as[Vector[String]].map(Multi.apply).map(v => Vector(key -> v))
          else if (json.isObject)
            json.hcursor.keys.fold(noEntries) { fields =>
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
        traverse(c, Seq.empty).map(c => Dict(c.toMap))

    }
}
