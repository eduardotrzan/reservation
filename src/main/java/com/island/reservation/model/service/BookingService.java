package com.island.reservation.model.service;

import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.dao.IRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}