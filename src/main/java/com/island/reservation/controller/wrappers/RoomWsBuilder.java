package com.island.reservation.controller.wrappers;

import com.island.reservation.ConversionUtils;
import com.island.reservation.controller.ws.RoomWs;
import com.island.reservation.model.entity.Room;
import com.island.reservation.model.entity.enums.RoomStatus;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
class RoomWsBuilder extends GenericWs<Room, RoomWs> {

	Room toBean(@NotNull RoomWs roomWs) {
		Room room = new Room();
		room.setTitle(roomWs.getTitle());
		room.setStatus(RoomStatus.toStatus(roomWs.getStatus()));
		room.setCreateDate(ConversionUtils.parseToCalendar(roomWs.getCreateDate()));
		room.setUpdateDate(ConversionUtils.parseToCalendar(roomWs.getUpdateDate()));
		return room;
	}

	RoomWs toWs(@NotNull Room room) {
		RoomWs roomWs = new RoomWs();
		roomWs.setTitle(room.getTitle());
		roomWs.setStatus(room.getStatus() == null ? null : room.getStatus().toString());
		roomWs.setCreateDate(ConversionUtils.parseToDate(room.getCreateDate()));
		roomWs.setUpdateDate(ConversionUtils.parseToDate(room.getUpdateDate()));
		return roomWs;
	}
}