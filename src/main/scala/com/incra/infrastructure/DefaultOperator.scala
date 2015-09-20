package com.incra.infrastructure

import scala.util.Try

/**
 * A default binary operator that returns the left operand if it evaluates without exception
 * to some value otherwise the right operand is evaluated and returned.
 *
 * @author Jeff Risberg
 * @since 07/22/13
 */
object DefaultOperator {

  implicit class DecoratedCodeBlock[T](value: => T) {
    def default(defaultValue: => T) = optional.getOrElse(defaultValue)

    def optional: Option[T] = Try(Option(value)).toOption.flatten
  }

}
