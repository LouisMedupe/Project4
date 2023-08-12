package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field fld = target.getClass().getDeclaredField(fieldName);

            if(!fld.isAccessible()) {
                fld.setAccessible(true);
                wasPrivate = true;
            }
            fld.set(target, toInject);
            if(wasPrivate) {
                fld.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}