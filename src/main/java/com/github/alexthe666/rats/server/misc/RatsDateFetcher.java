package com.github.alexthe666.rats.server.misc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@SuppressWarnings("unused")
public class RatsDateFetcher {

	public static boolean isStartOfHour() {
		return LocalTime.now().getMinute() == 0;
	}

	public static boolean isAprilFools() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1;
	}

	public static boolean isPirateDay() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.SEPTEMBER && date.getDayOfMonth() == 19;
	}

	public static boolean isHalloweenDay() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.OCTOBER && date.getDayOfMonth() == 31;
	}

	public static boolean isHalloweenSeason() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.OCTOBER && date.getDayOfMonth() > 10;
	}

	public static boolean isThanksgiving() {
		LocalDate date = LocalDate.now();
		return date.isEqual(LocalDate.of(date.getYear(), Month.NOVEMBER, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)));
	}

	public static boolean isAlexsBDay() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 20;
	}

	public static boolean isChristmasDay() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 25;
	}

	public static boolean isChristmasSeason() {
		LocalDate date = LocalDate.now();
		//after thanksgiving, before jan 10
		return date.isAfter(LocalDate.of(date.getYear(), Month.NOVEMBER, 1).with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)).with(TemporalAdjusters.next(DayOfWeek.FRIDAY)))
				|| date.getMonth() == Month.DECEMBER || (date.getMonth() == Month.JANUARY && date.getDayOfMonth() <= 10);
	}

	public static boolean isNewYearsEve() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.DECEMBER && date.getDayOfMonth() == 31;
	}

	public static boolean isNewYears() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.JANUARY && date.getDayOfMonth() == 1;
	}

	public static boolean isGizmosBDay() {
		LocalDate date = LocalDate.now();
		return date.getMonth() == Month.JANUARY && date.getDayOfMonth() == 28;
	}
}
