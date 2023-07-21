package io.github.zy945.util.jacksonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zy945.util.reflectUtil.ReflectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 参数1:object<br/>
     * 参数2:实体类.class<br/>
     * 返回:List<实体类><br/>
     *
     * @param fromValue   object
     * @param toValueType Job.class
     * @return Job
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(byte[] fromValue, Class<T> toValueType) throws IOException {
        return objectMapper.readValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return objectMapper.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T convertValue(JSONObject jsonObject, Class<T> toValueType) throws JsonProcessingException {
        return objectMapper.readValue(jsonObject.toString(), toValueType);
    }

    /**
     * 参数1:json数组<br/>
     * 参数2:实体类.class<br/>
     * 返回:List<实体类><br/>
     *
     * @param jsonArray jsonArray
     * @param tClass    Job.class
     * @return List<Job>
     */
    public static <T> List<T> convertValue(JSONArray jsonArray, Class<T> tClass) {
        TypeReference<List<T>> listTypeRef = ReflectUtils.getListTypeRef(tClass);
        return objectMapper.convertValue(jsonArray.toList(), listTypeRef);
    }


    /**
     * jsonObject转换为List<List><br/>
     *
     * @param jsonObject json对象
     * @return List<Object>
     */
    public static List<Object> getListByJsonObject(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        return jsonArray.toList();
    }


    /**
     * JsonArray转List
     *
     * @param jsonArray 提供的jsonArray
     * @param clazz     列表类型.class
     * @param <T>
     * @return List<T>
     */
    @SuppressWarnings("目前不知道是否有阻塞,慎用")
    public static <T> List<T> getListByJsonArray(JSONArray jsonArray, Class<T> clazz) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(jsonArray.toString(), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> fromJsonStr(String json, Class<T> clazz) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        return objectMapper.readValue(json, type);
    }

    public static <T> Map<String, Object> convertToHashMap(T entity) {
        Map<String, Object> hashMap = new HashMap<>();

        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(entity);
                hashMap.put(fieldName, fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

}