package com.alerf.mutils.dict

case class Dict(entries: Map[String, Entry], enclose: String = Dict.defaultEnclose) {
  def apply(key: String, params: (String, String)*): String =
    entry(key, params:_*).getOrElse(key)

  def entry(key: String, params: (String, String)*): Option[String] =
    entries.get(key).flatMap(_.text()).map {
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
