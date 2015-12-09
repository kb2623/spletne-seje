package org.sessionization;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.TemporalAccessor;

public interface TimePoint {

	default LocalDate getLocalDate() {
		return null;
	}

	default LocalTime getLocalTime() {
		return null;
	}

	default int getSecond() {
		LocalTime time = getLocalTime();
		if (time != null) {
			return time.getSecond();
		} else {
			return 0;
		}
	}

	default int getMinute() {
		LocalTime time = getLocalTime();
		if (time != null) {
			return time.getMinute();
		} else {
			return 0;
		}
	}

	default int getHour() {
		LocalTime time = getLocalTime();
		if (time != null) {
			return time.getHour();
		} else {
			return 0;
		}
	}

	default int getYear() {
		LocalDate date = getLocalDate();
		if (date != null) {
			return date.getYear();
		} else {
			return 0;
		}
	}

	default int getMonthValue() {
		LocalDate date = getLocalDate();
		if (date != null) {
			return date.getMonthValue();
		} else {
			return 0;
		}
	}

	default int getDayOfMonth() {
		LocalDate date = getLocalDate();
		if (date != null) {
			return date.getDayOfMonth();
		} else {
			return 0;
		}
	}

	default long getInNano() {
		LocalTime time = getLocalTime();
		long res = 0;
		if (time != null) {
			res = time.getHour() * 3600;
			res += time.getMinute() * 60;
			res += time.getSecond();
			res *= 1000000000;
		}
		return res;
	}

	default String printDate() {
		StringBuilder builder = new StringBuilder();
		TemporalAccessor tmp = getLocalDate();
		if (tmp != null) {
			builder.append(tmp.toString());
		} else {
			builder.append("0000-00-00");
		}
		builder.append('T');
		tmp = getLocalTime();
		if (tmp != null) {
			builder.append(tmp.toString());
		} else {
			builder.append("00:00:00");
		}
		return builder.toString();
	}

	default Period minusDate(TimePoint point) {
		return Period.of(getYear() - point.getYear(), getMonthValue() - point.getMonthValue(), getDayOfMonth() - point.getDayOfMonth());
	}

	default Duration minusTime(TimePoint point) {
		return Duration.ofNanos(getInNano() - point.getInNano());
	}
}
