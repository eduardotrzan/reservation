package com.island.reservation.model.service;

import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.exceptions.UnprocessableError;
import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.dao.IRoomDao;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

@Service
public class BookingService {

	@Autowired
	private IGuestDao guestDao;

	@Autowired
	private IBookingDao bookingDao;

	@Autowired
	private IRoomDao roomDao;

	public IGuestDao getGuestDao() {
		return guestDao;
	}

	public void setGuestDao(IGuestDao guestDao) {
		this.guestDao = guestDao;
	}

	public IBookingDao getBookingDao() {
		return bookingDao;
	}

	public void setBookingDao(IBookingDao bookingDao) {
		this.bookingDao = bookingDao;
	}

	public IRoomDao getRoomDao() {
		return roomDao;
	}

	public void setRoomDao(IRoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public List<Booking> findAll() {
		return (List<Booking>) this.bookingDao.findAll();
	}

	public boolean isAvailable(Calendar startDate, @NotNull Calendar endDate) throws Error {
		if (startDate == null) {
			throw new Error(
					ErrorCode.NULL_PARAMETER
					, "Check Availability Error"
					, "The Start Date cannot be null or empty while checking for availability."
					, null
			);
		}
		startDate.set(Calendar.HOUR, 12);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);

		if (endDate == null) {
			endDate = Calendar.getInstance();
			endDate.setTime(startDate.getTime());
			endDate.add(Calendar.MONTH, 1);
		}

		try {
			Booking booking = this.bookingDao.findAvailability(startDate, endDate);
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

	public Booking reserve(Booking booking, Guest guest) {
		return booking;
	}
}