package com.island.reservation.controller.wrappers;

import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.model.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class WsBuilder {

	private GuestWsBuilder guestWsBuilder;

	private RoomWsBuilder roomWsBuilder;

	private BookingWsBuilder bookingWsBuilder;

	@Autowired
	public void setGuestWsBuilder(GuestWsBuilder guestWsBuilder) {
		this.guestWsBuilder = guestWsBuilder;
	}

	@Autowired
	public void setRoomWsBuilder(RoomWsBuilder roomWsBuilder) {
		this.roomWsBuilder = roomWsBuilder;
	}

	@Autowired
	public void setBookingWsBuilder(BookingWsBuilder bookingWsBuilder) {
		this.bookingWsBuilder = bookingWsBuilder;
	}

	public List<BookingWs> getCompleteBookings(List<Booking> bookings) {
		List<BookingWs> completeBookingWss = bookings
				.parallelStream()
				.map(this::getCompleteBooking)
				.collect(Collectors.toList())
		;
		return completeBookingWss;
	}

	private BookingWs getCompleteBooking(Booking booking) {
		BookingWs bookingWs = this.bookingWsBuilder.toWs(booking);
		bookingWs.setGuest(this.guestWsBuilder.toWs(booking.getGuest()));
		bookingWs.setRoomWs(this.roomWsBuilder.toWs(booking.getRoom()));
		return bookingWs;
	}
}