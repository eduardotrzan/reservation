package com.island.reservation.controller.wrappers;

import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.model.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class WsBuilder {

	@Autowired
	private GuestWsBuilder guestWsBuilder;

	@Autowired
	private RoomWsBuilder roomWsBuilder;

	@Autowired
	private BookingWsBuilder bookingWsBuilder;

	public List<BookingWs> getCompleteBookings(List<Booking> bookings) {
		List<BookingWs> completeBookingWss = bookings
				.parallelStream()
				.map(this::getCompleteBooking)
				.collect(Collectors.toList())
		;
		return completeBookingWss;
	}

	public BookingWs getCompleteBooking(Booking booking) {
		BookingWs bookingWs = this.bookingWsBuilder.toWs(booking);
		bookingWs.setGuest(this.guestWsBuilder.toWs(booking.getGuest()));
		bookingWs.setRoom(this.roomWsBuilder.toWs(booking.getRoom()));
		return bookingWs;
	}
}