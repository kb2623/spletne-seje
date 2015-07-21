package org.oosqljet;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

import java.time.LocalDateTime;

@Table(autoId = true) public class SimpleClass {

	@Column
	private String name;
	@Column(name = {"sec_name", "we_will_not_need_this1", "we_will_not_need_this2"})
	private String surname;
	@Column(name = "date_of_birth")
	private LocalDateTime born;

	public SimpleClass(String name, String surname, LocalDateTime date) {
		this.name = name;
		this.surname = surname;
		born = date;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public LocalDateTime getDateBorn() {
		return born;
	}
}
