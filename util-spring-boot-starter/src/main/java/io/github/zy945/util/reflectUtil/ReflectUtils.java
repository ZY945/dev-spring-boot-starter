package io.github.zy945.util.reflectUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.*;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * 发射工具箱
 *
 * @author 伍六七
 * @date 2023/6/28 14:54
 */
public class ReflectUtils {
    // 定义一个方法，接受 Class<T> 参数并返回 TypeReference<List<T>> 实例

    /**
     * @param clazz Job.class
     * @return TypeReference<List < T>>
     */
    public static <T> TypeReference<List<T>> getListTypeRef(Class<T> clazz) {
        return new TypeReference<List<T>>() {
            @Override
            public Type getType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{clazz};
                    }

                    @Override
                    public Type getRawType() {
                        return List.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };
    }

    public static void setFieldValue(@Nonnull final Object object,
                                     @Nullable final Field field,
                                     @Nullable final Object value) {

        if (field == null || value == null) {
            return;
        }
        String msg =
                "Class '%s' field '%s' was defined with a different field type and caused a ClassCastException. "
                        + "The correct type is '%s' (current field value: '%s').";

        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Class<?> fieldType = field.getType();

            //the same type
            if (fieldType.equals(value.getClass())) {
                field.set(object, value);
                return;
            }

            //convert primitives
            if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
                field.set(object, toDoubleValue(value));
                return;
            }
            if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                field.set(object, toLongValue(value));
                return;
            }
            if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
                field.set(object, toIntValue(value));
                return;
            }
            if (float.class.isAssignableFrom(fieldType) || Float.class.isAssignableFrom(fieldType)) {
                field.set(object, toFloatValue(value));
                return;
            }
            if (short.class.isAssignableFrom(fieldType) || Short.class.isAssignableFrom(fieldType)) {
                field.set(object, toShortValue(value));
                return;
            }
            if (boolean.class.isAssignableFrom(fieldType)) {
                field.setBoolean(object, Boolean.valueOf(String.valueOf(value)));
                return;
            }
            if (BigDecimal.class.isAssignableFrom(fieldType)) {
                field.set(object, toBigDecimalValue(value));
                return;
            }

            //enum
            if (fieldType.isEnum()) {
                //noinspection unchecked, rawtypes
                field.set(object, Enum.valueOf((Class<Enum>) fieldType, String.valueOf(value)));
                return;
            }

            field.set(object, value);

        } catch (ClassCastException | IllegalAccessException e) {

            throw new RuntimeException(String.format(msg, object.getClass().getName(), field.getName(),
                    value.getClass().getName(), value));
        }
    }

    private static double toDoubleValue(final Object value) {

        if (double.class.isAssignableFrom(value.getClass()) || Double.class.isAssignableFrom(value.getClass())) {
            return (double) value;
        }

        return ((Number) value).doubleValue();
    }

    private static long toLongValue(final Object value) {

        if (long.class.isAssignableFrom(value.getClass()) || Long.class.isAssignableFrom(value.getClass())) {
            return (long) value;
        }

        return ((Number) value).longValue();
    }

    private static int toIntValue(final Object value) {

        if (int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())) {
            return (int) value;
        }

        return ((Number) value).intValue();
    }

    private static float toFloatValue(final Object value) {

        if (float.class.isAssignableFrom(value.getClass()) || Float.class.isAssignableFrom(value.getClass())) {
            return (float) value;
        }

        return ((Number) value).floatValue();
    }

    private static short toShortValue(final Object value) {

        if (short.class.isAssignableFrom(value.getClass()) || Short.class.isAssignableFrom(value.getClass())) {
            return (short) value;
        }

        return ((Number) value).shortValue();
    }

    private static BigDecimal toBigDecimalValue(final Object value) {
        if (String.class.isAssignableFrom(value.getClass())) {
            return new BigDecimal((String) value);
        }

        if (double.class.isAssignableFrom(value.getClass()) || Double.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((double) value);
        }

        if (int.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((int) value);
        }

        if (long.class.isAssignableFrom(value.getClass()) || Long.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((long) value);
        }

        if (float.class.isAssignableFrom(value.getClass()) || Float.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((float) value);
        }

        if (short.class.isAssignableFrom(value.getClass()) || Short.class.isAssignableFrom(value.getClass())) {
            return BigDecimal.valueOf((short) value);
        }

        String message = String.format("Cannot cast %s [%s] to %s.",
                value.getClass().getName(), value, BigDecimal.class);

        throw new ClassCastException(message);
    }
}
