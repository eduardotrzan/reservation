package com.island.reservation.controller.api;

import com.island.reservation.controller.wrappers.WsBuilder;
import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.service.BookingService;
import com.island.reservation.system.BookingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ReservationApiController {

	@Autowired
	private BookingConfig config;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private WsBuilder wsBuilder;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String home(HttpServletResponse response) {
		String message = String.format("Please check the README for this application to know the available endpoints on %s.", this.config.getServiceName());
		return message;
	}

	@RequestMapping(value = "/reservations", method = RequestMethod.GET)
	@ResponseBody
	public List<BookingWs> listBookings() {
		List<Booking> bookings = this.bookingService.findAll();
		return wsBuilder.getCompleteBookings(bookings);
	}
}
