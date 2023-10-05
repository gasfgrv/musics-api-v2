package com.github.gasfgrv.music_api.core.annotations

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotEmptyList.NotEmptyListValidator::class])
@MustBeDocumented
annotation class NotEmptyList(
  val message: String = "This field must have at least one non-null item",
  val groups: Array<KClass<Any>> = [],
  val payload: Array<KClass<Any>> = []
) {
  class NotEmptyListValidator : ConstraintValidator<NotEmptyList, List<String>> {
    override fun isValid(value: List<String>, context: ConstraintValidatorContext?): Boolean {
      if (value.isNotEmpty()) {
        return value.stream().anyMatch { it.isNullOrBlank() }.not()
      }

      return false
    }
  }
}
