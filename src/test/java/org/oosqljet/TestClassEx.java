package org.oosqljet;

import org.oosqljet.annotation.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestClassEx extends TestClass {

    @Column
	 private Collection collection;
    @Column
	 private List list;

    public TestClassEx(String name, String date) {
        super(name, date);
        List list = new ArrayList<>();
        list.add(new Integer(10));
        list.add(new Integer(43));
        list.add("hello");
        collection = this.list = list;
    }
}
