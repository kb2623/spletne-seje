package org.oosqljet;

import org.oosqljet.annotation.Entry;
import org.oosqljet.annotation.Table;

@Table(name = "test") public class TestClass {
    @Entry int pInt = 10;

    @Entry boolean pBool = false;

    @Entry double pDouble = 1.3;

    @Entry float pFloat = 1;

    @Entry char pChar = 'a';

    @Entry long pLong = 2;

	@Entry byte pByte = 2;

    @Entry short pShort = 2;

    @Entry int[] primitiveArray = {1, 2, 3};

    @Entry(primaryKey = true) private String name;

    @Entry public String date;

    @Entry Object[] array = {10, "asdf"};

	@Entry TestClass test;

    public TestClass(String name, String date) {
        this.name = name;
        this.date = date;
    }
}
