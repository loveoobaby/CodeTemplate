package com.yss.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 *
 */
public class refection_Unsafe {

    public static Unsafe getUnsafeInstance() {
        try {
            Class<?> clazz = Unsafe.class;
            Field f = clazz.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(clazz);
            return unsafe;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
