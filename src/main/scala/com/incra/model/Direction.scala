package com.incra.model

/**
 * Direction is ascending or descending.
 *
 * @author Jeff Risberg
 * @since 10/06/14
 */
object Direction extends Enumeration[String, Direction] {

  object Ascending extends Direction("ASC", 1)

  object Descending extends Direction("DESC", -1)

  val list = List(Ascending, Descending)
}

sealed abstract class Direction(val key: String, val toInt: Int) extends Enumerated[String]
