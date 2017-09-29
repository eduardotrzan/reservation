package com.island.reservation.model.service.interfaces;

import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IService {

	static <T> List<Error> validate(T t) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(t);

		if (violations == null || violations.isEmpty()) {
			return null;
		}

		List<Error> errors = violations
				.parallelStream()
				.map(violation -> new Error(
						ErrorCode.INVALID_PARAMETER
						, "Constraint Violation Error"
						, violation.getMessage()
						, null
				))
				.collect(Collectors.toList());
		return errors;
	}
}
