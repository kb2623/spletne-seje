package org.spletneseje.database;

import org.junit.Before;
import org.junit.Test;
import org.spletneseje.database.annotation.Field;
import org.spletneseje.database.annotation.Table;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class DataBaseTest {

    DataBase db;

    @Before
    public void setUp() {
        //TODO
    }

    @Test
    public void testInsert() {

    }

    @Test
    public void testReflections() {
        TestAnotations anotations = new TestAnotations("klemen", "1.1.1992");
        java.lang.reflect.Field[] fields = anotations.getClass().getDeclaredFields();
        System.out.println("Fields with Field Annotation");
        for (java.lang.reflect.Field fild : fields) {
            if (fild.isAnnotationPresent(Field.class)) System.out.println(fild.getName());
        }
        System.out.println("\nMethods with Field Annotation:");
        Method[] methods = anotations.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Field.class)) System.out.println(method.getName());
        }
    }

    @Table
    class TestAnotations {

        @Field(primaryKey = true)
        private String name;

        @Field
        public String date;

        public TestAnotations(String name, String date) {
            this.name = name;
            this.date = date;
        }

        @Field
        public String nameMethod() {
            return name;
        }

        @Field
        private String dateMethod() {
            return date;
        }
    }

}