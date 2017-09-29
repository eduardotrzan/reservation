package com.island.reservation.model.service.interfaces;

import com.island.reservation.exceptions.Error;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.Guest;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

public interface IBookingService extends IService {

	List<Booking> findAll();
	boolean isAvailable(Calendar startDate, @NotNull Calendar endDate) throws Error;
	Booking reserve(Booking booking, Guest guest) throws Error;
}
