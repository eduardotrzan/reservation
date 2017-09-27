package com.island.reservation.exceptions;

public enum ErrorCode {
	GENERAL("error.general"),

	DATABASE("error.database"),
	ENTITY_NOT_FOUND("error.entity.not.found"),
	SAVE("error.save"),
	UPDATE("error.update"),
	DELETE("error.delete"),

	INVALID_PARAMETER("error.invalid.parameter"),
	MISMATCHING_TYPE("error.mismatching.type"),
	NULL_PARAMETER("error.null.parameter"),

	API_VALIDATION("error.api.validation"),
	API_INVALID_JSON ("error.invalid.json"),
	API_UNAVAILABLE("error.api.unavailable"),

	ENDPOINT_NOTFOUND("error.endpoint.notfound"),
	ENDPOINT_WRONG("error.endpoint.wrong")

	;

	private final String code;

	ErrorCode(final String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
}