package com.github.alexthe666.rats.server.misc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

@SuppressWarnings("unused")
public class RatsDateFetcher {

	public static boolean isStartOfHour() {
		LocalTime time = LocalTime.now();
		return time.get(ChronoField.MINUTE_OF_HOUR) == 0;
	}

	public static boolean isAprilFools() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 4 && date.get(ChronoField.DAY_OF_MONTH) == 1;
	}

	public static boolean isHalloweenDay() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 10 && date.get(ChronoField.DAY_OF_MONTH) == 31;
	}

	public static boolean isHalloweenSeason() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 10 && date.get(ChronoField.DAY_OF_MONTH) > 10;
	}

	public static boolean isThanksgiving() {
		LocalDate date = LocalDate.now();
		return date.isEqual(LocalDate.of(date.getYear(), 11, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)));
	}

	public static boolean isAlexsBDay() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 12 && date.get(ChronoField.DAY_OF_MONTH) == 20;
	}

	public static boolean isChristmasDay() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 12 && date.get(ChronoField.DAY_OF_MONTH) == 25;
	}

	public static boolean isChristmasSeason() {
		LocalDate date = LocalDate.now();
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		int day = date.get(ChronoField.DAY_OF_MONTH);
		//after thanksgiving, before jan 10
		return date.isAfter(LocalDate.of(date.getYear(), 11, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)).with(TemporalAdjusters.next(DayOfWeek.FRIDAY)))
				|| month == 12 || (month == 1 && day <= 10);
	}

	public static boolean isNewYearsEve() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 12 && date.get(ChronoField.DAY_OF_MONTH) == 31;
	}

	public static boolean isNewYears() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 1 && date.get(ChronoField.DAY_OF_MONTH) == 1;
	}

	public static boolean isGizmosBDay() {
		LocalDate date = LocalDate.now();
		return date.get(ChronoField.MONTH_OF_YEAR) == 1 && date.get(ChronoField.DAY_OF_MONTH) == 28;
	}
}
