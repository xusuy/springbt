package com.xusy.springbt.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return map;
    }
    /**
     * Map转成实体对象
     *
     * @param map    要转化的map
     * @param tClass 对象的class
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> tClass) {
        Object obj = null;
        try {
            if (map != null) {
                obj = tClass.newInstance();
                Field[] fields = tClass.getDeclaredFields();
                for (Field field : fields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getType() == String.class) {
                        field.set(obj, map.get(field.getName()) == null ? null : String.valueOf(map.get(field.getName())));
                    } else if (field.getType() == Date.class) {
                        if (map.get(field.getName()) != null) {
                            field.set(obj, DateHelper.string2Date(String.valueOf(map.get(field.getName()))));
                        }
                    } else if (field.getType() == Boolean.class) {
                        if (map.get(field.getName()) != null) {
                            field.set(obj, "true".equals(String.valueOf(map.get(field.getName()))));
                        }
                    } else if (field.getType() == BigDecimal.class) {
                        if (map.get(field.getName()) != null) {
                            field.set(obj, new BigDecimal(String.valueOf(map.get(field.getName()))));
                        }
                    } else if (field.getType() == Integer.class) {
                        if (map.get(field.getName()) != null) {
                            field.set(obj, new BigDecimal(String.valueOf(map.get(field.getName()))).intValue());
                        }
                    } else {
                        field.set(obj, map.get(field.getName()));
                    }

                }
            }
        } catch (InstantiationException | IllegalAccessException | ParseException exception) {
            exception.printStackTrace();
            return null;
        }
        return obj;
    }

}
