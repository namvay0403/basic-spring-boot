package com.nam.demojpa.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

  private int min;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true;
    }

    var years = ChronoUnit.YEARS.between(value, LocalDate.now());

    return years >= min;
  }
}
