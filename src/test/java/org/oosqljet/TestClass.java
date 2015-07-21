package org.oosqljet;

import org.oosqljet.annotation.Column;
import org.oosqljet.annotation.Table;

@Table(name = "test")
public class TestClass {
	@Column
	int pInt = 10;

	@Column
	boolean pBool = false;

	@Column
	double pDouble = 1.3;

	@Column
	float pFloat = 1;

	@Column
	char pChar = 'a';

	@Column
	long pLong = 2;

	@Column
	byte pByte = 2;

	@Column
	short pShort = 2;

	@Column
	int[] primitiveArray = {1, 2, 3};

	@Column(primaryKey = true)
	private String name;

	@Column
	public String date;

	@Column
	Object[] array = {10, "asdf"};

	@Column
	TestClass test;

	public TestClass(String name, String date) {
		this.name = name;
		this.date = date;
	}
}
