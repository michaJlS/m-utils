package com.alerf.mutils.dict

import scala.util.Random

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
