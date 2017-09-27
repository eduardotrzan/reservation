package com.island.reservation.exceptions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class Error {

	private ErrorCode errorCode;

	private String title;

	private String description;

	private Error errorDetails;

	private Exception exception;

	public Error(
			@NotNull ErrorCode errorCode,
			@NotNull String title,
			@NotNull String description,
			@Null Error details) {
		this(errorCode, title, description, details, null);
	}

	public Error(
			@NotNull ErrorCode errorCode,
			@NotNull String title,
			@NotNull String description,
			@Null Error errorDetails,
			@Null Exception exception
	) {
		this.errorCode = errorCode;
		this.title = title;
		this.description = description;
		this.errorDetails = errorDetails;
		this.exception = exception;
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Error getErrorDetails() {
		return errorDetails;
	}

	public abstract String getDetails();
}