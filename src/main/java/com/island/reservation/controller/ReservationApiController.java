package com.island.reservation.controller;

import com.island.reservation.model.service.BookingService;
import com.island.reservation.system.BookingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ReservationApiController {

	@Autowired
	private BookingConfig config;

	@Autowired
	private BookingService bookingService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String home(HttpServletResponse response) {
		String message = String.format("Please check the README for this application to know the available endpoints on %s.", this.config.getServiceName());
		return message;
	}
}
