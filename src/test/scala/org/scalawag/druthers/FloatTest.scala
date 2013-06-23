package org.scalawag.druthers

object FloatTest {
  case class Opts(val aopt:Float,
                  val bopt:Float)
}

import FloatTest._

class FloatTest extends ParserTest {

  test("short - present") {
    succeed[Opts]("-a 4.2 -b7.1",ShortOptions()) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("short - space must delimit value, fail") {
    fail[Opts]("-a 4.2 -b7.1",ShortOptions().withSpaceDelimitsValue(Some(true))) {
      case Seq(MissingValue(flag),UnknownKey("-7"),UnknownKey("-."),UnknownKey("-1")) =>
        flag.key should be ("b")
    }
  }

  test("short - space must delimit value, pass") {
    succeed[Opts]("-a 4.2 -b 7.1",ShortOptions().withSpaceDelimitsValue(Some(true))) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("short - space must not delimit value, fail") {
    fail[Opts]("-a 4.2 -b7.1",ShortOptions().withSpaceDelimitsValue(Some(false))) {
      case Seq(MissingValue(flag)) =>
        flag.key should be ("a")
    }
  }

  test("short - space must not delimit value, pass") {
    succeed[Opts]("-a4.2 -b7.1",ShortOptions().withSpaceDelimitsValue(Some(false))) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("short - absent") {
    fail[Opts]("-a 4.2",ShortOptions()) {
      case Seq(MissingRequiredKey(flag)) => flag.key should be ("b")
    }
  }

  test("short - cluster, rest is arg (not other keys)") {
    succeed[Opts]("-a 4.2 -b7.1",ShortOptions().withClustering(true)) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("short - invalid delimited") {
    fail[Opts]("-a 4.2 -b x",ShortOptions()) {
      case Seq(InvalidValue(flag,"x",_)) => flag.key should be ("b")
    }
  }

  test("short - invalid non-delimited") {
    fail[Opts]("-a 4.2 -bx",ShortOptions()) {
      case Seq(InvalidValue(flag,"x",_)) => flag.key should be ("b")
    }
  }

  test("long - present") {
    succeed[Opts]("--aopt 4.2 --bopt=7.1",Opts(4.2f,7.1f),"",LongOptions())
  }

  test("long - equals required, fail") {
    fail[Opts]("--aopt=4.2 --bopt 7.1",LongOptions().withEqualDelimitsValue(Some(true))) {
      case Seq(MissingValue(flag)) =>
        flag.key should be ("bopt")
    }
  }

  test("long - equals required, pass") {
    succeed[Opts]("--aopt=4.2 --bopt=7.1",LongOptions().withEqualDelimitsValue(Some(true))) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("long - equals forbidden, fail") {
    fail[Opts]("--aopt=4.2 --bopt 7.1",LongOptions().withEqualDelimitsValue(Some(false))) {
      case Seq(UnknownKey("--aopt=4.2")) => // should fail like this
    }
  }

  test("long - equals must not delimit value, pass") {
    succeed[Opts]("--aopt 4.2 --bopt 7.1",LongOptions().withEqualDelimitsValue(Some(false))) { case(opts,remains) =>
      opts should be (Opts(4.2f,7.1f))
      remains should be (Array.empty)
    }
  }

  test("long - absent") {
    fail[Opts]("--aopt 4.2",LongOptions()) {
      case Seq(MissingRequiredKey(flag)) => flag.key should be ("bopt")
    }
  }

  test("long - specify illegal value") {
    fail[Opts]("--aopt=notanum",LongOptions()) {
      case Seq(InvalidValue(flag,"notanum",_)) =>
        flag.key should be ("aopt")
    }
  }
}

/* druthers -- Copyright 2013 Justin Patterson -- All Rights Reserved */
