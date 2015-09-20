package com.incra.infrastructure

import com.escalatesoft.subcut.inject.BindingId

/**
 * Common Binding Ids, to avoid putting strings into the code that the compiler can't check.
 *
 */
object BindingIds {
  implicit def bindingIdToString(bindingId: BindingId): String = bindingId.bindingName

  object MySQL extends BindingId

  object Postgres extends BindingId

  object Mongo extends BindingId

  object Redshift extends BindingId

}
