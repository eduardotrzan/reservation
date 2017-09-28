package com.island.reservation.exceptions;

import javax.validation.constraints.NotNull;

public class InternalServerError extends Error {

	public InternalServerError(@NotNull Exception details) {
		this(details.getMessage());
	}

	public InternalServerError(@NotNull String details) {
		super(
				ErrorCode.API_UNAVAILABLE
				, "Internal Server Error"
				, "An error has happened while processing the request."
				, details
		);
	}
}