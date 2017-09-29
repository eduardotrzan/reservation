package com.island.reservation.controller.errors;

import com.island.reservation.exceptions.Error;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorWsBuilder {

	public ErrorWs toWs(@NotNull Error error) {
		ErrorWs errorWs = new ErrorWs();
		if (error == null) {
			return errorWs;
		}
		errorWs.setErrorCode(error.getErrorCode().toString());
		errorWs.setTitle(error.getTitle());
		errorWs.setDescription(error.getDescription());
		errorWs.setDetails(error.getDetails());
		return errorWs;
	}

	public List<ErrorWs> toWs(@NotNull List<Error> errors) {
		List<ErrorWs> errorWss = new ArrayList<>();
		if (errors == null) {
			return errorWss;
		}
		for (Error error : errors) {
			errorWss.add(this.toWs(error));
		}
		return errorWss;
	}
}
