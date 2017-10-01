package com.island.reservation.controller.api;

import com.island.reservation.controller.errors.ErrorWsBuilder;
import com.island.reservation.controller.wrappers.WsBuilder;
import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.exceptions.UnprocessableError;
import com.island.reservation.model.service.interfaces.IBookingService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Calendar;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ReservationApiControllerTest {

	@Mock
	private IBookingService bookingService;

	@Autowired
	private WsBuilder wsBuilder;

	@Mock
	private ErrorWsBuilder errorWsBuilder;

	@InjectMocks
	private ReservationApiController api;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		MockitoAnnotations.initMocks(this);
		RestAssuredMockMvc.standaloneSetup(api);
	}

	@Test
	public void availability_testStartEndDates_Unprocessable() {
		try {
			Error error = new UnprocessableError(
					ErrorCode.DATABASE
					, "Find Booking Error"
					, "An error has happened while finding Booking Availability."
					, ""
			);
			when(this.bookingService.isAvailable(any(Calendar.class), any(Calendar.class)))
					.thenThrow(error);
			given().
					param("startDate", "2017-01-01").
					param("endDate", "2017-01-01").
					when().
					get("/availability").
					then().
					statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
		} catch (Exception e) {
			fail("Failed on testing API availability, having an existing booking in the period.", e);
		}
	}

	@Test
	public void availability_testStartEndDates_BadRequest() {
		try {
			Error error = new Error(
					ErrorCode.DATABASE
					, "Find Booking Error"
					, "An error has happened while finding Booking Availability."
					, ""
			);
			when(this.bookingService.isAvailable(any(Calendar.class), any(Calendar.class)))
					.thenThrow(error);
			given().
					param("startDate", "2017-01-01").
					param("endDate", "2017-01-01").
					when().
					get("/availability").
					then().
					statusCode(HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			fail("Failed on testing API availability, having an existing booking in the period.", e);
		}
	}

	@Test
	public void availability_testStartEndDates_ok() {
		given().
				param("startDate", "2017-01-01").
				param("endDate", "2017-01-01").
				when().
				get("/availability").
				then().
				statusCode(HttpStatus.OK.value());
	}
}
