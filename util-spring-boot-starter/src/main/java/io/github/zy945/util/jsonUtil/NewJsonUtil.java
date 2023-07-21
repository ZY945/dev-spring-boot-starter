package io.github.zy945.util.jsonUtil;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author 伍六七
 * @date 2023/6/11 12:02
 */
public class NewJsonUtil {

    /**
     * Object转json字符串
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String objToJsonStr(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(obj);
    }


    //////////////////字符串转其他//////////////////

    /**
     * json字符串转JSONObject
     *
     * @param jsonStr
     * @return
     */
    public static JSONObject jsonStrToJSONObject(String jsonStr) {
        return new JSONObject(jsonStr);
    }

    /**
     * json字符串转Object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T jsonStrToObj(String json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, clazz);
    }


    //////////////////json对象转其他//////////////////

    /**
     * JSONObject转Object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T jsonObjToObj(JSONObject json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json.toString(), clazz);
    }

    /**
     * JSONOArray转List
     *
     * @param jsonArray
     * @param targetType
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> List<T> jsonArrayToList(JSONArray jsonArray, Class<T> targetType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, targetType);
        return objectMapper.readValue(jsonArray.toString(), type);
    }


}
