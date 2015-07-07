package org.oosqljet;

import org.oosqljet.annotation.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestClassEx extends TestClass {

    @Entry private Collection collection;
    @Entry private List list;

    public TestClassEx(String name, String date) {
        super(name, date);
        List list = new ArrayList<>();
        list.add(new Integer(10));
        list.add(new Integer(43));
        list.add("hello");
        collection = this.list = list;
    }
}
