package com.alerf.mutils.dict.marshalling

import org.scalatest.FunSpec
import org.scalatest.Matchers._
import io.circe.parser.decode
import com.alerf.mutils.dict.Dict

import scala.io.Source


class JsonCirceTest extends FunSpec {

  import JsonCirce._

  describe("JsonCirce") {
    it("provides unmarshaller from json for Dict") {
      val d = decode[Dict](Source.fromResource("dict/sample.json").mkString)
      d shouldBe 'right
      d.right.map { dict =>
        dict("a.b.i") shouldBe "value-i"
        dict("a.d") should fullyMatch regex("value-d-[12]")
      }
    }
  }

}
