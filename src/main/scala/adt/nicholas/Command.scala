package com.lookageek.scala
package adt.nicholas

// Command is a sum type because it is kind-of, an enumeration
// Command = Face OR Start OR Stop
// but since we have Chain too, in addition to being just the sum-type which Direction is
// Command is also an Algebraic Data Type
// an algebraic data type is a potentially recursive sum type of product types
// Command = Face OR Start OR Stop OR (Command AND Command)
// adding the valid state transitions at the type level, using the types which do not have
// values
sealed trait Command[Before, After]

object Command {
  // -> this is a product type
  // now we are baking in the valid state transitions into the types
  case class Face(dir: Direction) extends Command[Idle, Idle]
  case object Start extends Command[Idle, Moving]
  case object Stop extends Command[Moving, Idle]

  // chaining commands by composition, this makes it a product type
  // product type is an AND type, an aggregation of values
  // Chain = Command AND Command
  case class Chain[A, B, C](cmd1: Command[A, B], cmd2: Command[B, C]) extends Command[A, C]

  // DSL with proper state transition checks in place
  implicit class Compose[A, B](cmd1: Command[A, B]) {
    def ~>[C](cmd2: Command[B, C]): Command[A, C] =
      Command.Chain(cmd1, cmd2)
  }

  // use it as start ~> stop
  val startStop: Command[Idle, Idle] = Command.Start ~> Command.Stop

  // but this will not compile
  // val startStart: Command[Idle, Idle] = Command.Start ~> Command.Start
}

// composing commands is not always safe, there are some compositions that does
// not make sense
// start AND start, stop AND stop, start AND face(north) are some examples
// how can we catch these things at type level at compile time?
// some way of tracking state
// using these two final abstract classes
// final abstract classes are just types which cannot have any value, sort of Void type
// in Haskell
// these types are also Witness types in GADT
final abstract class Idle
final abstract class Moving

// Generalised Algebraic Data Type
// A GADT is a sum type with one or more witness types, each equipped with a type equality
// A Witness Type describes properties of a sum type's branches at the type level
// Type equality is information available to compiler about each witness type, allowing it
// to refine pattern matches
// for example -> Option is a GADT with Some & None as witness type

// finally, ADTs and GADTs make it impossible to represent illegal values statically


// the various flavours of union types, look at one-note for notes
// here is a simple Discriminated Union, a Sum Type
sealed trait IntOrString
object IntOrString {
  case class I(value: Int) extends IntOrString
  case class S(value: String) extends IntOrString


  def printUnion(value: IntOrString): Unit = value match {
    case I(i) => println(s"int: $i")
    case S(s) => println(s"string: $s")
  }
}
