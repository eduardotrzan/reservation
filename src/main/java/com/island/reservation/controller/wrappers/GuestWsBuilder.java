package com.island.reservation.controller.wrappers;

import com.island.reservation.ConversionUtils;
import com.island.reservation.controller.ws.GuestWs;
import com.island.reservation.model.entity.Guest;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
class GuestWsBuilder extends GenericWs<Guest, GuestWs> {

	Guest toBean(@NotNull GuestWs guestWs) {
		Guest guest = new Guest();
		guest.setFirstName(guestWs.getFirstName());
		guest.setLastName(guestWs.getLastName());
		guest.setEmail(guestWs.getEmail());
		guest.setCreateDate(ConversionUtils.parseToCalendar(guestWs.getCreateDate()));
		guest.setUpdateDate(ConversionUtils.parseToCalendar(guestWs.getUpdateDate()));
		return guest;
	}

	GuestWs toWs(@NotNull Guest guest) {
		GuestWs guestWs = new GuestWs();
		guestWs.setFirstName(guest.getFirstName());
		guestWs.setLastName(guest.getLastName());
		guestWs.setEmail(guest.getEmail());
		guestWs.setCreateDate(ConversionUtils.parseToDate(guest.getCreateDate()));
		guestWs.setUpdateDate(ConversionUtils.parseToDate(guest.getUpdateDate()));
		return guestWs;
	}
}