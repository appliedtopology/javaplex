package edu.stanford.math.plex4.utility;

import java.lang.reflect.Array;

public class MemoryUtility {
	public static Object deepArrayCopy(Object object) {
        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            Class<?> component = object.getClass().getComponentType();
            Object newInnerArray = Array.newInstance(component, length);
            for (int i = 0; i < length; i++) {
                Object elem = deepArrayCopy(Array.get(object, i));
                Array.set(newInnerArray, i, elem);
            }
            return newInnerArray;
        } else {
            return object;
        }
	}
}
