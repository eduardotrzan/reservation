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
import java.util.UUID;

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

	public boolean isAvailable(Calendar startDate, @NotNull Calendar endDate) throws Error {
		return this.isAvailable(startDate, endDate, null);
	}

	@Transactional(readOnly = true)
	public boolean isAvailable(Calendar startDate, @NotNull Calendar endDate, String uuid) throws Error {
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
			noonEndDate.add(Calendar.MONTH, 1);
		} else {
			noonEndDate = ConversionUtils.noon(endDate);
		}

		try {
			Booking booking;
			if (uuid == null) {
				booking = this.bookingDao.findAvailability(noonStartDate, noonEndDate);
			} else {
				booking = this.bookingDao.findAvailabilityExceptUuid(noonStartDate, noonEndDate, uuid);
			}
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
		bookingToSave = this.bookingDao.save(bookingToSave);
		return bookingToSave;
	}

	@Transactional
	public Booking modify(Booking booking) throws Error {
		this.validateBooking(booking);

		String uuid = booking.getUuid();
		this.validateUuid(uuid);

		Booking bookingToModify = this.bookingDao.findByUuid(uuid);
		if (bookingToModify == null) {
			throw new Error(
					ErrorCode.ENTITY_NOT_FOUND
					, "Modify Reservation Error"
					, "Booking with Reference " + uuid + " was not found in the system."
					, null
			);
		}

		Calendar noonStartDate = ConversionUtils.noon(booking.getStartDate());
		Calendar noonEndDate = ConversionUtils.noon(booking.getEndDate());

		if (bookingToModify.getStartDate().equals(noonStartDate) &&
				bookingToModify.getEndDate().equals(noonEndDate)) {
			throw new Error(
					ErrorCode.INVALID_PARAMETER
					, "Modify Reservation Error"
					, "Booking with same Start and End dates in the system."
					, "Start Date: " + booking.getStartDate() + " | End Date: " + booking.getEndDate()
			);
		}

		boolean isAvailable = this.isAvailable(noonStartDate, noonEndDate, uuid);
		if (!isAvailable) {
			throw new Error(
					ErrorCode.INVALID_REQUIREMENT
					, "Modify Reservation Error"
					, "The requested modification to Start and end Dates is not available."
					, "Start Date: " + booking.getStartDate() + " | End Date: " + booking.getEndDate()
			);
		}

		bookingToModify.setStartDate(noonStartDate);
		bookingToModify.setEndDate(noonEndDate);
		bookingToModify.setUpdateDate(Calendar.getInstance());

		bookingToModify = this.bookingDao.save(bookingToModify);
		return bookingToModify;
	}

	@Transactional
	public Booking cancel(String uuid) throws Error {
		this.validateUuid(uuid);

		Booking booking = this.bookingDao.findByUuid(uuid);
		if (booking == null) {
			throw new Error(
					ErrorCode.ENTITY_NOT_FOUND
					, "Cancel Reservation Error"
					, "Booking with Reference " + uuid + " was not found in the system."
					, null
			);
		}

		booking.setStatus(BookingStatus.CANCELLED);
		booking.setUpdateDate(Calendar.getInstance());
		booking = this.bookingDao.save(booking);

		return booking;
	}

	private void validateUuid(String uuid) throws  Error {
		if (uuid == null || uuid.trim().isEmpty()) {
			throw new Error(
					ErrorCode.NULL_PARAMETER
					, "Cancellation Error"
					, "The booking Identifier needs to be informed."
					, null
			);
		}

		try {
			UUID.fromString(uuid);
		} catch (IllegalArgumentException exception) {
			throw new Error(
					ErrorCode.INVALID_PARAMETER
					, "Cancellation Error"
					, "Booking with Reference " + uuid + " is not valid."
					, exception.getMessage()
			);
		}
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