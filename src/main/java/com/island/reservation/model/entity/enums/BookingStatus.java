package com.island.reservation.model.entity.enums;

public enum BookingStatus {
	PENDING,
	ACTIVE,
	CONFIRMED,
	CANCELLED
	;

	public static BookingStatus toStatus(String status) {
		if (status != null) {
			if (BookingStatus.ACTIVE.toString().equalsIgnoreCase(status)) {
				return BookingStatus.ACTIVE;
			} else if (BookingStatus.CONFIRMED.toString().equalsIgnoreCase(status)) {
				return BookingStatus.CONFIRMED;
			} else if (BookingStatus.CANCELLED.toString().equalsIgnoreCase(status)) {
				return BookingStatus.CANCELLED;
			}
		}

		return BookingStatus.PENDING;
	}
}
