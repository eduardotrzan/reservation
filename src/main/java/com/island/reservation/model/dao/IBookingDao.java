package com.island.reservation.model.dao;

import com.island.reservation.model.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryDefinition(domainClass = Booking.class, idClass = Integer.class)
public interface IBookingDao extends CrudRepository<Booking, Integer> {
}