package com.island.reservation.exceptions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class Error extends Exception {

	private ErrorCode errorCode;

	private String title;

	private String description;

	private String details;

	private Exception exception;

	public Error(
			@NotNull ErrorCode errorCode,
			@NotNull String title,
			@NotNull String description,
			@Null String details) {
		this(errorCode, title, description, details, null);
	}

	public Error(
			@NotNull ErrorCode errorCode,
			@NotNull String title,
			@NotNull String description,
			@Null String details,
			@Null Exception exception
	) {
		this.errorCode = errorCode;
		this.title = title;
		this.description = description;
		this.details = details == null ? "" : details;
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

	@Override
	public String getMessage() {
		return this.title + "\n\n" + this.getDescription();
	}

	public String getDetails() {
		if (this.exception != null) {
			return this.details + "\n\n" + this.exception.getMessage();
		}
		return this.details;
	}
}