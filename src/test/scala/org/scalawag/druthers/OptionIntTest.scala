package org.scalawag.druthers

object OptionIntTest {
  case class Opts(val aopt:Option[Int],
                  val bopt:Option[Int])
}

import OptionIntTest._

class OptionIntTest extends ParserTest {

  test("short - present") {
    succeed[Opts]("-a 42 -b7",SHORT) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("short - collapse prohibited, fail") {
    fail[Opts]("-a 42 -b7",SHORT.withCollapsedValuesProhibited) {
      case Seq(MissingValue(flag),UnknownKey("-7")) =>
        flag.key should be ("b")
    }
  }

  test("short - collapse prohibited, pass") {
    succeed[Opts]("-a 42 -b 7",SHORT.withCollapsedValuesProhibited) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("short - collapse required, fail") {
    fail[Opts]("-a 42 -b7",SHORT.withCollapsedValuesRequired) {
      case Seq(MissingValue(flag)) =>
        flag.key should be ("a")
    }
  }

  test("short - collapse required, pass") {
    succeed[Opts]("-a42 -b7",SHORT.withCollapsedValuesRequired) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("short - absent") {
    succeed[Opts]("-a 42",Opts(Some(42),None),"",SHORT)
  }

  test("short - cluster, rest is arg (not other keys)") {
    succeed[Opts]("-a 42 -b7",SHORT.withClustering) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("short - invalid delimited") {
    fail[Opts]("-a 42 -b x",SHORT) {
      case Seq(InvalidValue(flag,"x",_)) => flag.key should be ("b")
    }
  }

  test("short - invalid non-delimited") {
    fail[Opts]("-a 42 -bx",SHORT) {
      case Seq(InvalidValue(flag,"x",_)) => flag.key should be ("b")
    }
  }

  test("long - present") {
    succeed[Opts]("--aopt 42 --bopt=7",Opts(Some(42),Some(7)),"",LONG)
  }

  test("long - collapse prohibited, fail") {
    fail[Opts]("--aopt=42 --bopt 7",LONG.withCollapsedValuesProhibited) {
      case Seq(UnknownKey("--aopt=42")) => // should fail like this
    }
  }

  test("long - collapse prohibited, pass") {
    succeed[Opts]("--aopt 42 --bopt 7",LONG.withCollapsedValuesProhibited) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("long - collapse required, fail") {
    fail[Opts]("--aopt=42 --bopt 7",LONG.withCollapsedValuesRequired) {
      case Seq(MissingValue(flag)) =>
        flag.key should be ("bopt")
    }
  }

  test("long - collapse required, pass") {
    succeed[Opts]("--aopt=42 --bopt=7",LONG.withCollapsedValuesRequired) { case(opts,remains) =>
      opts should be (Opts(Some(42),Some(7)))
      remains should be (Array.empty)
    }
  }

  test("long - absent") {
    succeed[Opts]("--aopt 42",Opts(Some(42),None),"",LONG)
  }

  test("long - specify illegal value") {
    fail[Opts]("--aopt=notanum",LONG) {
      case Seq(InvalidValue(flag,"notanum",_)) =>
        flag.key should be ("aopt")
    }
  }
}

/* druthers -- Copyright 2013 Justin Patterson -- All Rights Reserved */
