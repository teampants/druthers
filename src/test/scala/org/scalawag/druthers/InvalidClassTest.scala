package org.scalawag.druthers

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

object InvalidClassTest {
  case class EverythingGood(a:Boolean,
                            b:Counter,
                            c:String,
                            d:Option[String],
                            e:Seq[String],
                            f:Int,
                            g:Option[Int],
                            h:Seq[Int],
                            i:Float,
                            j:Option[Float],
                            k:Seq[Float])

  case class BadSeqBoolean(a:Seq[Boolean])
  case class BadOptionBoolean(a:Option[Boolean])
  case class BadSeqCounter(a:Seq[Boolean])
  case class BadOptionCounter(a:Option[Boolean])
  case class BadBigInt(a:BigInt)
}

import InvalidClassTest._

class InvalidClassTest extends FunSuite with ShouldMatchers {

  test("accept everyting good") {
    new Parser[EverythingGood]
  }

  test("reject Seq[Counter] field") {
    intercept[IllegalArgumentException] {
      new Parser[BadSeqCounter]
    }
  }

  test("reject Option[Counter] field") {
    intercept[IllegalArgumentException] {
      new Parser[BadOptionCounter]
    }
  }

  test("reject Seq[Boolean] field") {
    intercept[IllegalArgumentException] {
      new Parser[BadSeqBoolean]
    }
  }

  test("reject Option[Boolean] field") {
    intercept[IllegalArgumentException] {
      new Parser[BadOptionBoolean]
    }
  }

  test("reject BigInt field") {
    intercept[IllegalArgumentException] {
      new Parser[BadBigInt]
    }
  }
}

/* druthers -- Copyright 2013 Justin Patterson -- All Rights Reserved */
