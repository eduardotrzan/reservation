package com.island.reservation.controller.api;

import com.island.reservation.controller.errors.ErrorWs;
import com.island.reservation.controller.errors.ErrorWsBuilder;
import com.island.reservation.controller.wrappers.WsBuilder;
import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.service.BookingService;
import com.island.reservation.system.BookingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationApiController {

	@Autowired
	private BookingConfig config;

	@Autowired
	private BookingService bookingService;

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
		List<Booking> bookings = this.bookingService.findAll();
		List<BookingWs> bookingWss = wsBuilder.getCompleteBookings(bookings);
		return ResponseEntity.status(HttpStatus.OK).body(bookingWss);
	}
}
