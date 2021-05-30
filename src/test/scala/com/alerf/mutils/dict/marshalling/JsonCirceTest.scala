package com.alerf.mutils.dict.marshalling

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import io.circe.parser.decode
import com.alerf.mutils.dict.Dict

import scala.io.Source


class JsonCirceTest extends AnyFunSpec with Matchers {

  import JsonCirce._

  describe("JsonCirce") {
    it("provides unmarshaller from json for Dict") {
      val d = decode[Dict](Source.fromResource("dict/sample.json").mkString)
      d.isRight shouldBe true
      d.map { dict =>
        dict("a.b.i") shouldBe "value-i"
        dict("a.d") should fullyMatch regex ("value-d-[12]")
      }
    }
  }

}
