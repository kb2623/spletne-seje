package org.sessionization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public interface TimePoint {

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

	default LocalDate getLocalDate() {
		return null;
	}

	default LocalTime getLocalTime() {
		return null;
	}

	default LocalDateTime getLocalDateTime() {
		LocalDate date = getLocalDate();
		LocalTime time = getLocalTime();
		if (date == null) {
			date = LocalDate.MIN;
		}
		if (time == null) {
			time = LocalTime.MIDNIGHT;
		}
		return date.atTime(time);
	}

	default String printDate() {
		return getLocalDateTime().format(formatter);
	}

	default long secBetwene(TimePoint point) {
		return Math.abs(minus(point));
	}

	default long minus(TimePoint point) {
		return ChronoUnit.SECONDS.between(point.getLocalDateTime(), getLocalDateTime());
	}
}
