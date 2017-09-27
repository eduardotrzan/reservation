package com.island.reservation.model.entity.enums;

public enum  RoomStatus {
	UNAVAILABLE,
	AVAILABLE
	;

	public static RoomStatus toStatus(String status) {
		if (status != null) {
			if (RoomStatus.UNAVAILABLE.toString().equalsIgnoreCase(status)) {
				return RoomStatus.UNAVAILABLE;
			}
		}

		return RoomStatus.AVAILABLE;
	}
}