package com.island.reservation;

import java.util.Calendar;
import java.util.Date;

public final class ConversionUtils {

	private ConversionUtils() { }

	public static Calendar parseToCalendar(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Date parseToDate(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.getTime();
	}
}
