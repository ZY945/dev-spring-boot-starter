package io.github.zy945.util.beanUtils;

import java.util.Objects;

/**
 * @author 伍六七
 * @date 2023/6/1 17:18
 */
public class ObjectUtil {

    /**
     * 判断对象是否为null
     *
     * @param o
     * @param name
     */
    public static void checkObjNotNull(Object o, String name) {
        Objects.requireNonNull(o, () -> name + "is Null");
    }

    /**
     * 字符串不能为null,""," "
     *
     * @param str
     * @param name
     * @throws IllegalArgumentException
     */
    public static void checkStrName(String str, String name) throws IllegalArgumentException {
        if (str != null && !str.isBlank()) {
        } else {
            throw new IllegalArgumentException(name + "is not a empty,it may be null , \" \" or \"\"");
        }
    }

    /**
     * 字符串不能为null,""
     *
     * @param str
     * @param name
     * @throws IllegalArgumentException
     */
    public static void checkStrNonEmpty(String str, String name) throws IllegalArgumentException {
        if (str != null && !str.isEmpty()) {
        } else {
            throw new IllegalArgumentException(name + "is not a empty,it may be null or \"\"");
        }
    }
}
