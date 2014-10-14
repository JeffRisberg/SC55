package com.incra.model

/**
 * An <i>Enumeration</i> is an ordered collection of enumerated items.
 *
 * @author Jeff Risberg
 * @since 09/27/14
 */
trait Enumeration[K, T <: Enumerated[K]] extends Serializable {
  val list: List[T]

  private lazy val map: Map[K, T] = list.map((enumerated: T) => enumerated.key -> enumerated).toMap

  final def valueOf(key: K): T = map(key)
}
