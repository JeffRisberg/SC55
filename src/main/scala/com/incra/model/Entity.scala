package com.incra.model

/**
 * The defining characteristic of an entity is that it has an immutable identity that is unique within the system.
 *
 * @author Peter Potts
 * @since 08/23/12
 */
trait Entity[I] {
  val key: I

  def ===(other: Any) = other match {
    case that: Entity[_] => this.key == that.key
    case _ => false
  }
}
