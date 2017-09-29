package com.island.reservation.model.service.impl;

import com.island.reservation.ConversionUtils;
import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.exceptions.UnprocessableError;
import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.dao.IRoomDao;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.Guest;
import com.island.reservation.model.entity.Room;
import com.island.reservation.model.entity.enums.BookingStatus;
import com.island.reservation.model.service.interfaces.IBookingService;
import com.island.reservation.model.service.interfaces.IGuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

@Service
public class BookingService implements IBookingService {

	@Autowired
	private IGuestService guestService;

	@Autowired
	private IBookingDao bookingDao;

	@Autowired
	private IRoomDao roomDao;

	public List<Booking> findAll() {
		return (List<Booking>) this.bookingDao.findAll();
	}

	@Transactional(readOnly = true)
	public boolean isAvailable(Calendar startDate, @NotNull Calendar endDate) throws Error {
		if (startDate == null) {
			throw new Error(
					ErrorCode.NULL_PARAMETER
					, "Check Availability Error"
					, "The Start Date cannot be null or empty while checking for availability."
					, null
			);
		}

		Calendar noonStartDate = ConversionUtils.noon(startDate);

		Calendar noonEndDate;
		if (endDate == null) {
			noonEndDate = ConversionUtils.noon(noonStartDate);
			endDate.add(Calendar.MONTH, 1);
		} else {
			noonEndDate = ConversionUtils.noon(endDate);
		}

		try {
			Booking booking = this.bookingDao.findAvailability(noonStartDate, noonEndDate);
			return booking == null;
		} catch (Exception exception) {
			throw new UnprocessableError(
					ErrorCode.DATABASE
					, "Find Booking Error"
					, "An error has happened while finding Booking Availability."
					, exception.getMessage()
			);
		}
	}

	@Transactional
	public Booking reserve(final Booking booking, final Guest guest) throws Error {
		this.validateBooking(booking);

		Calendar noonStartDate = ConversionUtils.noon(booking.getStartDate());
		Calendar noonEndDate = ConversionUtils.noon(booking.getEndDate());



		Room campsite = this.roomDao.findByTitle("Campsite");
		boolean isAvailable = this.isAvailable(noonStartDate, noonStartDate);
		if (!isAvailable) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Reservation Error"
					, "The " + campsite.getTitle() + " is not available."
					, null
			);
		}

		Guest existingGuest = this.guestService.saveIfNotExists(guest);

		Booking bookingToSave = new Booking();
		bookingToSave.setStartDate(noonStartDate);
		bookingToSave.setEndDate(noonEndDate);
		bookingToSave.setStatus(BookingStatus.ACTIVE);
		bookingToSave.setRoom(campsite);
		bookingToSave.setGuest(existingGuest);
		Booking saveBooking = this.bookingDao.save(bookingToSave);
		return saveBooking;
	}

	private void validateBooking(Booking booking) throws Error {
		if (booking == null) {
			throw new Error(
					ErrorCode.NULL_PARAMETER
					, "Reservation Error"
					, "Booking information is null."
					, null
			);
		}

		if (booking.getStartDate() == null || booking.getEndDate() == null) {
			throw new Error(
					ErrorCode.INVALID_PARAMETER
					, "Reservation Error"
					, "Booking parameter contains one or more invalid information."
					, "Start Date: " + booking.getStartDate() + " | End Date: " + booking.getEndDate()
			);
		}

		Calendar noonStartDate = ConversionUtils.noon(booking.getStartDate());
		Calendar noonEndDate = ConversionUtils.noon(booking.getEndDate());

		if (noonStartDate.after(noonEndDate)) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Reservation Error"
					, "Booking Start Date cannot be after End Date on the reservation."
					, null
			);
		}

		Calendar nowNoon = ConversionUtils.noon(Calendar.getInstance());
		Calendar minimumDaysArrival = ConversionUtils.noon(noonStartDate);
		minimumDaysArrival.add(Calendar.DAY_OF_MONTH, -1);
		if (nowNoon.compareTo(minimumDaysArrival) >= 0) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Reservation Error"
					, "Booking Start Date must be a minimum of 1 day ahead of arrival."
					, null
			);
		}

		Calendar maximumDaysArrival = ConversionUtils.noon(Calendar.getInstance());
		maximumDaysArrival.add(Calendar.MONTH, 1);
		if (maximumDaysArrival.before(noonStartDate)) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Reservation Error"
					, "Booking Start Date must be a maximum of 1 month before arrival."
					, null
			);
		}

		Calendar maxReservationDays = ConversionUtils.noon(noonStartDate);
		maxReservationDays.add(Calendar.DAY_OF_MONTH, 3);
		if (noonEndDate.after(maxReservationDays)) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Reservation Error"
					, "Booking cannot be for more than 3 days."
					, null
			);
		}
	}
}