package com.island.reservation.controller.wrappers;

import com.island.reservation.ConversionUtils;
import com.island.reservation.controller.ws.BookingWs;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.enums.BookingStatus;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
class BookingWsBuilder extends GenericWs<Booking, BookingWs> {

	Booking toBean(@NotNull BookingWs bookingWs) {
		Booking booking = new Booking();
		booking.setUuid(bookingWs.getUuid());
		booking.setStatus(BookingStatus.toStatus(bookingWs.getStatus()));
		booking.setCreateDate(ConversionUtils.parseToCalendar(bookingWs.getCreateDate()));
		booking.setUpdateDate(ConversionUtils.parseToCalendar(bookingWs.getUpdateDate()));
		booking.setStartDate(ConversionUtils.parseToCalendar(bookingWs.getStartDate()));
		booking.setEndDate(ConversionUtils.parseToCalendar(bookingWs.getEndDate()));
		return booking;
	}

	BookingWs toWs(@NotNull Booking booking) {
		BookingWs bookingWs = new BookingWs();
		bookingWs.setUuid(booking.getUuid());
		bookingWs.setStatus(booking.getStatus() == null ? null : booking.getStatus().toString());
		bookingWs.setCreateDate(ConversionUtils.parseToDate(booking.getCreateDate()));
		bookingWs.setUpdateDate(ConversionUtils.parseToDate(booking.getUpdateDate()));
		bookingWs.setStartDate(ConversionUtils.parseToDate(booking.getStartDate()));
		bookingWs.setEndDate(ConversionUtils.parseToDate(booking.getEndDate()));
		return bookingWs;
	}
}