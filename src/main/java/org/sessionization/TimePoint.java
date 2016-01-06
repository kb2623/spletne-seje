package org.sessionization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public interface TimePoint {

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

	default LocalDate getLocalDate() {
		return LocalDate.MIN;
	}

	default LocalTime getLocalTime() {
		return LocalTime.MIDNIGHT;
	}

	default LocalDateTime getLocalDateTime() {
		return getLocalDate().atTime(getLocalTime());
	}

	default String printDate() {
		return getLocalDateTime().format(formatter);
	}

	default long secBetwene(TimePoint point) {
		return Math.abs(ChronoUnit.SECONDS.between(getLocalDateTime(), point.getLocalDateTime()));
	}
}
