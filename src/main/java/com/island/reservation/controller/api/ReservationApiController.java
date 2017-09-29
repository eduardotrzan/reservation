package com.island.reservation.controller.api;

import com.island.reservation.ConversionUtils;
import com.island.reservation.controller.errors.ErrorWs;
import com.island.reservation.controller.errors.ErrorWsBuilder;
import com.island.reservation.controller.wrappers.WsBuilder;
import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.controller.ws.ReservationWs;
import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.exceptions.InternalServerError;
import com.island.reservation.exceptions.UnprocessableError;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.Guest;
import com.island.reservation.model.service.interfaces.IBookingService;
import com.island.reservation.system.BookingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ReservationApiController {

	@Autowired
	private BookingConfig config;

	@Autowired
	private IBookingService bookingService;

	@Autowired
	private WsBuilder wsBuilder;

	@Autowired
	private ErrorWsBuilder errorWsBuilder;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity home() {
		ErrorWs errorWs = new ErrorWs();
		errorWs.setErrorCode(ErrorCode.ENDPOINT_WRONG.toString());
		errorWs.setTitle("Access Error");
		errorWs.setDescription(String.format("Please check the README for this application to know the available endpoints on %s.", this.config.getServiceName()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorWs);
	}

	@RequestMapping(value = "/reservations", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity listBookings() {
		try {
			List<Booking> bookings = this.bookingService.findAll();
			List<BookingWs> bookingWss = this.wsBuilder.getCompleteBookings(bookings);
			return ResponseEntity.status(HttpStatus.OK).body(bookingWss);
		} catch (Exception exception) {
			return this.getErrorResponse(exception);
		}
	}

	@RequestMapping(value = "/availability", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity availability(
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam() Date startDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date endDate) {
		try {
			boolean isAvailable = this.bookingService.isAvailable(
					ConversionUtils.parseToCalendar(startDate),
					ConversionUtils.parseToCalendar(endDate)
			);
			return ResponseEntity.status(HttpStatus.OK).body(
					Map.of("availability", isAvailable)
			);
		} catch (Exception exception) {
			return this.getErrorResponse(exception);
		}
	}

	@RequestMapping(value = "/reservation", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity reserve(@RequestBody ReservationWs reservationWs) {
		try {
			List<ErrorWs> errorWss = this.validate(reservationWs);
			if (errorWss != null && !errorWss.isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body(errorWss);
			}

			Booking booking = new Booking();
			booking.setStartDate(ConversionUtils.parseToCalendar(reservationWs.getStartDate()));
			booking.setEndDate(ConversionUtils.parseToCalendar(reservationWs.getEndDate()));

			Guest guest = new Guest();
			guest.setFirstName(reservationWs.getFirstName());
			guest.setLastName(reservationWs.getLastName());
			guest.setEmail(reservationWs.getEmail());

			Booking bookingReservation = this.bookingService.reserve(booking, guest);
			BookingWs bookingWs = this.wsBuilder.getCompleteBooking(bookingReservation);
			return ResponseEntity.status(HttpStatus.OK).body(bookingWs);
		} catch (Exception exception) {
			return this.getErrorResponse(exception);
		}
	}

	@RequestMapping(value = "/reservation/{uuid}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity cancel(@PathVariable(value="uuid") String uuid) {
		try {
			Booking cancelledReservation = this.bookingService.cancel(uuid);
			BookingWs bookingWs = this.wsBuilder.getCompleteBooking(cancelledReservation);
			return ResponseEntity.status(HttpStatus.OK).body(bookingWs);
		} catch (Exception exception) {
			return this.getErrorResponse(exception);
		}
	}

	private ResponseEntity getErrorResponse(Exception exception) {
		HttpStatus httpStatus;
		ErrorWs errorWs;
		if (exception instanceof UnprocessableError) {
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			errorWs = this.errorWsBuilder.toWs((Error) exception);
		} else if (exception instanceof Error) {
			httpStatus = HttpStatus.BAD_REQUEST;
			errorWs = this.errorWsBuilder.toWs((Error) exception);
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			errorWs = this.errorWsBuilder.toWs(new InternalServerError(exception));
		}

		return ResponseEntity
				.status(httpStatus)
				.body(errorWs);
	}

	private <T> List<ErrorWs> validate(T t) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(t);

		if (violations == null || violations.isEmpty()) {
			return null;
		}

		List<Error> errors = violations
				.parallelStream()
				.map(violation -> new Error(
						ErrorCode.API_VALIDATION
						, "Constraint Violation Error"
						, violation.getMessage()
						, null
				))
				.collect(Collectors.toList());
		return this.errorWsBuilder.toWs(errors);
	}
}
