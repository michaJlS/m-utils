package com.alerf.mutils.marshalling

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

import io.circe._, io.circe.parser._

class JsonCirceTest extends AnyFunSpec with Matchers {
  describe("JsonCirce") {
    describe("Provides Either decoder that") {
      it("should decode Right when Left is invalid") {
        val json = parse("1").right.get
        JsonCirce.eitherDecoder[String, Int].decodeJson(json) shouldBe Right(Right(1))
      }
      it("should decode Left when Right is valid") {
        val json = parse("\"2\"").right.get
        JsonCirce.eitherDecoder[String, List[String]].decodeJson(json) shouldBe Right(Left("2"))
      }
      it("should decode Right when both are valid") {
        val json = parse("3").right.get
        JsonCirce.eitherDecoder[Int, Int].decodeJson(json) shouldBe Right(Right(3))
      }
      it("should fail when both are invalid") {
        val json = parse("{\"a\": 3}").right.get
        JsonCirce.eitherDecoder[Int, String].decodeJson(json).isLeft shouldBe true
      }
    }
  }
}
