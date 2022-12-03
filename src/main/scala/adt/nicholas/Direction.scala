package com.lookageek.scala
package adt.nicholas

// Direction is a sum-type because it is an enumeration
// sum type is a OR type with tagged union of values
sealed trait Direction

object Direction {
  case object North extends Direction
  case object South extends Direction
  case object East extends Direction
  case object West extends Direction


  def label(d: Direction): String = {
    d match {
      case Direction.North => "north"
      case Direction.South => "south"
      case Direction.East => "east"
      case Direction.West => "west"
    }
  }
}
