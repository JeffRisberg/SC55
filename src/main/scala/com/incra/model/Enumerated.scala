package com.incra.model

/**
 * An enumeration is an ordered collection of enumerated items.
 *
 * @author Jeff Risberg
 * @since 09/23/14
 */
trait Enumerated[I] extends Entity[I] {
  final override def toString = key.toString
}

