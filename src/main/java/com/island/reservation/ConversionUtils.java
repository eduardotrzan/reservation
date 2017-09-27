package com.island.reservation;

import java.util.Calendar;
import java.util.Date;

public final class ConversionUtils {

	private ConversionUtils() { }

	public static Calendar parseToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		return calendar;
	}

	public static Date parseToDate(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.getTime();
	}
}
