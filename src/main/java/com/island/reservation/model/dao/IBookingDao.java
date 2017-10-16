package com.island.reservation.model.dao;

import com.island.reservation.model.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Calendar;

@NoRepositoryBean
public interface IBookingDao extends CrudRepository<Booking, Integer> {

	boolean findAvailability(Calendar startDate, Calendar endDate);
	boolean findAvailabilityExceptUuid(Calendar startDate, Calendar endDate, String uuid);
	Booking findByUuid(String uuid);
}