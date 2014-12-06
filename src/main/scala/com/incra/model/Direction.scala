package com.incra.model

/**
 * Direction is ascending or descending.
 *
 * @author Jeff Risberg
 * @since 10/06/14
 */
object Direction extends scala.Enumeration {

  val Ascending = Direction("ASC", 1)

  val Descending = Direction("DESC", -1)

  case class Direction(value: String, toInt: Int) extends Val(nextId, value)

  final def withKey(k: String): Direction = {
    values.iterator.map(_.asInstanceOf[Direction]).find(_.value == k).get
  }

  final def list: List[Direction] = values.toList.map(_.asInstanceOf[Direction])
}
