package com.incra.infrastructure

import com.incra.infrastructure.Logging
import com.incra.infrastructure.UseResource._

/**
 * A convenient control structure for automatic resource management.
 *
 * If you have non-atomic resource allocations, want to chain resource allocation together, or
 * desire lazy execution, use [[Resource]] instead. It provides a superset of this behavior,
 * and is much safer for more complex scenarios.
 *
 * What is the difference between this class and the Resource class provided in Scala?
 *
 * @author various
 * @since 12/15/14
 */
object UseResource extends Logging {
  def using[R, T](resource: R)(release: R => Unit)(use: R => T): T = {
    try {
      use(resource)
    } finally {
      try {
        release(resource)
      } catch {
        case e: Exception =>
          log.error(e.getMessage, e)
          throw e
      }
    }
  }
}
