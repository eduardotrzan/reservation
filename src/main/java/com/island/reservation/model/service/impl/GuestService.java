package com.island.reservation.model.service.impl;

import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.entity.Guest;
import com.island.reservation.model.service.interfaces.IGuestService;
import com.island.reservation.model.service.interfaces.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService implements IGuestService {

	@Autowired
	private IGuestDao guestDao;

	public Guest saveIfNotExists(Guest guest) throws Error {
		if (guest == null) {
			throw new Error(
					ErrorCode.NULL_PARAMETER
					, "Guest Information Error"
					, "Guest is null."
					, null
			);
		}

		List<Error> errors = IService.validate(guest);
		if (errors != null) {
			throw new Error(
					ErrorCode.INVALID_PARAMETER
					, "Guest Information Error"
					, "Guest contains one or more invalid information."
					, errors.toString()
			);
		}

		Guest existingGuest = this.guestDao.findByEmail(guest.getEmail());
		if (existingGuest == null) {
			existingGuest = this.guestDao.save(guest);
		}

		return existingGuest;
	}
}