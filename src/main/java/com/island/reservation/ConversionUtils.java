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


	public static Calendar noon(Date date) {
		if (date == null) {
			return null;
		}
		Calendar noon = Calendar.getInstance();
		noon.setTime(date);
		noon.set(Calendar.HOUR, 12);
		noon.set(Calendar.MINUTE, 0);
		noon.set(Calendar.SECOND, 0);
		return noon;
	}

	public static Calendar noon(Calendar calendar) {
		return calendar == null ? null : ConversionUtils.noon(calendar.getTime());
	}
}
