package com.alerf.mutils

import scala.util.Random

package object dict {
  case class Dict(entries: Map[String, Entry], enclose: String = Dict.defaultEnclose) {
    def apply(key: String, params: (String, String)*): String =
      entry(key, params:_*).getOrElse(key)

    def entry(key: String, params: (String, String)*): Option[String] =
      entries.get(key).flatMap(_.text).map {
        params.foldLeft(_){
          case (txt, (tag, rep)) => txt.replace(enclose + tag + enclose, rep)
        }
      }
  }

  object Dict {
    val defaultEnclose = "$"

    def fromEntries(entries: (String, Entry)*): Dict =
      Dict(entries.toMap)

    def fromSingles(entries: (String, String)*): Dict =
      Dict(
        entries.map {
          case (k, v) => k -> Single(v)
        }.toMap
      )
  }

  sealed trait Entry {
    def text(): Option[String]
  }

  case class Single(txt: String) extends Entry {
    override def text(): Option[String] = Some(txt)
  }

  case class Multi(texts: Vector[String])(implicit val randomGen: Random) extends Entry {
    override def text(): Option[String] =
      if (texts.isEmpty)
        None
      else
        Some(texts(randomGen.nextInt(texts.length)))
  }
}
