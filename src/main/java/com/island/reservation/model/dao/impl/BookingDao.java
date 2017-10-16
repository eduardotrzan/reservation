package com.island.reservation.model.dao.impl;

import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.entity.Booking;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Calendar;

@Repository
public class BookingDao extends SimpleJpaRepository<Booking, Integer> implements IBookingDao {

	private final EntityManager entityManager;

	public BookingDao(EntityManager em) {
		super(Booking.class, em);
		this.entityManager = em;
	}

	@Override
	public boolean findAvailability(Calendar startDate, Calendar endDate) {
		return this.findAvailabilityExceptUuid(startDate, endDate, null);
	}

	@Override
	public boolean findAvailabilityExceptUuid(Calendar startDate, Calendar endDate, String uuid) {
		boolean hasUuid = uuid != null && !uuid.isEmpty();

		String sql = "SELECT b FROM Booking b " +
				"WHERE ((?1 BETWEEN b.startDate AND b.endDate) " +
				"OR (?2 BETWEEN b.startDate AND b.endDate)) " +
				"OR (b.startDate >= ?1 AND b.endDate <= ?2) " +
				" AND NOT(b.status = 'CANCELLED')";

		if (hasUuid) {
			sql += " AND NOT(b.uuid = ?3) ";
		}

		TypedQuery<Booking> query = this.entityManager.createQuery(sql, Booking.class);
		query.setParameter(1, startDate);
		query.setParameter(2, endDate);

		if (hasUuid) {
			query.setParameter(3, uuid);
		}
		query.setMaxResults(1);
		Booking booking;
		try {
			booking = query.getSingleResult();
		} catch (NoResultException nre) {
			booking = null;
		} catch (Exception e) {
			throw e;
		}
		return booking == null;
	}

	@Override
	public Booking findByUuid(String uuid) {
		String sql = "SELECT b FROM Booking b " +
				"WHERE b.uuid = ?1 " +
				" AND NOT(b.status = 'CANCELLED')";

		TypedQuery<Booking> query = this.entityManager.createQuery(sql, Booking.class);
		query.setParameter(1, uuid);
		query.setMaxResults(1);
		Booking booking;
		try {
			booking = query.getSingleResult();
		} catch (NoResultException nre) {
			booking = null;
		} catch (Exception e) {
			throw e;
		}
		return booking;
	}
}
