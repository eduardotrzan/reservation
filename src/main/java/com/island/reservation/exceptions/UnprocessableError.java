package com.island.reservation.exceptions;

import javax.validation.constraints.NotNull;

public class UnprocessableError extends Error {

	public UnprocessableError(ErrorCode errorCode, String title, String description, @NotNull Exception details) {
		this(errorCode, title, description, details.getMessage());
	}

	public UnprocessableError(ErrorCode errorCode, String title, String description, @NotNull String details) {
		super(
				errorCode
				, title
				, description
				, details
		);
	}
}