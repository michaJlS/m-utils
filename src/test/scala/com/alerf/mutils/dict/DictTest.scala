package com.alerf.mutils.dict

import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.util.Random

class DictTest extends FunSpec {

  describe("Dict") {
      describe("for existing key") {
        it("returns the entry value ") {
          val d = Dict.fromSingles("a" -> "b", "c" -> "d")
          d("a") shouldBe "b"
        }
        it("replaces tokens with provided values") {
          val d = Dict.fromSingles("a" -> "b", "c" -> "d$s$", "complex" -> "lorem $muspi$ $rolod$")
          d("c", "s" -> "om") shouldBe "dom"
          d("complex", "rolod" -> "dolor", "muspi" -> "ipsum") shouldBe "lorem ipsum dolor"
        }
        it("returns one of possible values for Multi entry") {
          val r = new Random(12312312312L)
          val d = Dict.fromEntries("c" -> Multi(Vector("x", "y", "z"))(r), "k" -> Single("o"))

          d("c") shouldBe "x"
          d("c") shouldBe "z"
          d("c") shouldBe "y"
        }
      }
    describe("for non-existing key") {
      it("defaults to the key itself") {
        val d = Dict.fromSingles("a" -> "b", "c" -> "d")
        d("z") shouldBe("z")
      }
    }
    describe("entry method") {
      describe("for non-existing key") {
        it("returns None") {
          val d = Dict.fromSingles("a" -> "b", "c" -> "d")
          d.entry("z") shouldBe None
        }
      }
    }
  }


}
