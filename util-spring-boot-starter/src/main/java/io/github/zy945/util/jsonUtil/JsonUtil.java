package io.github.zy945.util.jsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.zy945.util.commonUtil.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author 伍六七
 * @date 2023/5/20 23:40
 * android 自带原生org.json 自定义JSON处理工具类
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 根据json的Str格式来去除指定的字段
     *
     * @param json  json字符串
     * @param field 需要去掉的字段
     * @return 去掉字段后json格式
     * @throws IOException
     */
    public static String removeClassField(String json, String field) throws IOException {
        // 解析 JSON 字符串为 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(json);

        if (rootNode.isArray()) {
            // 如果 JSON 是一个数组，则逐个删除数组元素中的 _class 字段
            for (JsonNode element : rootNode) {
                ((ObjectNode) element).remove(field);
            }
        } else {
            // 否则，直接删除根节点中的 _class 字段
            ((ObjectNode) rootNode).remove(field);
        }

        // 将结果序列化为字符串返回
        return objectMapper.writeValueAsString(rootNode);
    }

    public static String formatJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>对象/数组列表转JSON字符串</p>
     */
    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return toJSONObject(obj).toString();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>对list转为JSONArray,对对象转为JSONObject</p>
     */
    @SuppressWarnings("rawtypes")
    private static Object toJSONObject(Object object)
            throws JSONException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (object == null) {
            return null;
        }
        if (object instanceof JSONObject || object instanceof JSONArray) {
            return object;
        }
        // 基本数据类型非数组
        if (isBaseType(object.getClass()) && !object.getClass().isArray()) {
            return object;
        } else if (object instanceof Map) { //如果为Map
            Map map = (Map) object;
            JSONObject jsonObject = new JSONObject();
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                jsonObject.put(String.valueOf(key), toJSONObject(value));
            }
            return jsonObject;
        } else if (object instanceof List) {// 为List
            List list = (List) object;
            JSONArray jsonArray = new JSONArray();
            for (Object obj : list) {
                jsonArray.put(toJSONObject(obj));
            }
            return jsonArray;
        } else if (object.getClass().isArray()) { // 为数组
            int length = Array.getLength(object);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < length; i++) {
                jsonArray.put(toJSONObject(Array.get(object, i)));
            }
            return jsonArray;
        } else {
            JSONObject jsonObject = new JSONObject();
            Class<?> clazz = object.getClass();
            parseObject(clazz, jsonObject, object);
            return jsonObject;
        }
    }

    private static void parseObject(Class<?> clazz, JSONObject jsonObject, Object object)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, JSONException {
        if (clazz == null) {
            return;
        }
        // 通过反射获取到对象的所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 判断如果给定field.getModifiers()参数包含transient修饰符
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            // 获取属性的属性名
            String fieldName = field.getName();
            // getDeclaredMethod 获得类声明的命名的方法，但无法获取父类的字段，从类中获取了一个方法后，可以用 invoke() 方法来调用这个方法
            Method method = clazz.getDeclaredMethod("get" + StringUtil.captureName(fieldName));
            if (method != null) {
                jsonObject.put(fieldName, toJSONObject(method.invoke(object)));
            }
        }
        //clazz 的父类解析，继承关系时获取父类信息
        parseObject(clazz.getSuperclass(), jsonObject, object);
    }

    /**
     * <p>判断是否为基本类型并且不为数组</p>
     */
    private static boolean isBaseType(Class<?> clazz) {
        // isPrimitive 原始类型，isAssignableFrom 判断是否为某个类的类型
        if (clazz.isPrimitive()
                || String.class.isAssignableFrom(clazz)// clazz 是否能强转为 String 类型
                || Integer.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Byte.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Json字符串转成对象
     *
     * @param json   json字符串
     * @param tClazz 列表中的数据类型
     * @return 转化后的数据对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, Class<T> tClazz) throws Exception {
        if (json == null || "".equals(json) || "".equals(json.trim())) {
            throw new Exception("入参json数据为空，请检查");
        }
        // 基本数据类型包括字符串 且不为 基本数据类型包括字符串数组
        if (isBaseType(tClazz) && !tClazz.isArray()) {
            throw new Exception("入参tClazz为基本类型，无法反序列化");
        }
        // 进行json串基本检查，判断是json对象还是JSONArray
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        if (json.startsWith("{")) {
            // json是对象
            jsonObject = new JSONObject(json);
        } else if (json.startsWith("[")) {
            // json是JSONArray
            jsonArray = new JSONArray(json);
        } else {
            throw new Exception("json数据非标准格式，请检查");
        }
        // 如果是JSONObject 直接返回
        if (JSONObject.class.isAssignableFrom(tClazz)) {
            return (T) jsonObject;
        }
        if (JSONArray.class.isAssignableFrom(tClazz)) {
            return (T) jsonArray;
        }
        if (tClazz.isArray()) {// tClazz 表示数组类
            return createArr(jsonArray, tClazz);
        } else if (List.class.isAssignableFrom(tClazz)) {
            List<Object> list = new ArrayList<>();
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                list.add(jsonArray.get(i));
            }
            return (T) list;
        } else if (Set.class.isAssignableFrom(tClazz)) {
            Set<Object> set = new HashSet();
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                set.add(jsonArray.get(i));
            }
            return (T) set;
        }
        if (Map.class.isAssignableFrom(tClazz)) {
            Map<String, Object> map = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, jsonObject.get(key));
            }
            return (T) map;
        } else {
            if (jsonObject == null) {
                throw new Exception("json数据格式无法转换到" + tClazz);
            }
            T t = createObject(jsonObject, tClazz);
            return t;
        }
    }

    /**
     * 创建数组
     *
     * @param jsonArray jsonArray
     * @param tClazz    tClazz 表示数组类
     */
    @SuppressWarnings("unchecked")
    private static <T> T createArr(JSONArray jsonArray, Class<T> tClazz) throws Exception {
        int len = jsonArray.length();
        // System.out.println(tClazz + " " + jsonArray);
        //创建具有指定组件类型和长度的新数组
        Object arr = Array.newInstance(tClazz.getComponentType(), len);
        for (int i = 0; i < len; i++) {
            Object obj = jsonArray.get(i);
            if (isBaseType(obj.getClass()) && !obj.getClass().isArray()) {
                Array.set(arr, i, (int) obj);
            } else if (obj instanceof JSONObject) {
                JSONObject jsonObjectNext = (JSONObject) obj;
                Array.set(arr, i, createObject(jsonObjectNext, tClazz.getComponentType()));
            } else if (obj instanceof JSONArray) {
                JSONArray jsonArrayNext = (JSONArray) obj;
                Array.set(arr, i, createArr(jsonArrayNext, tClazz.getComponentType()));
            }
        }
        return (T) arr;
    }

    private static <T> T createObject(JSONObject jsonObject, Class<T> tClazz) throws Exception {
        // 创建 tClazz 对象对应类的实例
        T t = tClazz.newInstance();
        assignField(jsonObject, tClazz, t);
        return t;
    }

    private static <T> void assignField(JSONObject jsonObject, Class<?> tClazz, T t) throws Exception {
        if (tClazz == null) {
            return;
        }
        // 获得 tClazz 类声明的所有字段
        Field[] fields = tClazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取 此Field对象表示的字段的名称
            String fieldName = field.getName();
            // getType()：返回一个Class 对象，它标识了此 Field 对象所表示字段的声明类型，如：String、Integer
            Class<?> filedClazz = field.getType();
            if (jsonObject.isNull(fieldName)) {
                continue;
            }
            // 获取字段fieldName对应的值value
            Object value = jsonObject.opt(fieldName);
            if (isBaseType(filedClazz) || JSONObject.class.isAssignableFrom(filedClazz)
                    || JSONArray.class.isAssignableFrom(filedClazz)) {
                setterObject(tClazz, fieldName, filedClazz, t, value);
            } else if (filedClazz.isArray()) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Object arr = createArr(jsonArray, filedClazz);
                    setterObject(tClazz, fieldName, filedClazz, t, arr);
                }
            } else if (List.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Type typeClass = field.getGenericType();
                    List list = createList(typeClass, jsonArray);
                    setterObject(tClazz, fieldName, filedClazz, t, list);
                }
            } else if (Set.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Type typeClass = field.getGenericType();
                    Set set = createSet(typeClass, jsonArray);
                    setterObject(tClazz, fieldName, filedClazz, t, set);
                }
            } else if (Map.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONObject) {
                    Type typeClass = field.getGenericType();
                    JSONObject jsonObj = (JSONObject) value;
                    Map map = createMap(typeClass, jsonObj);
                    setterObject(tClazz, fieldName, filedClazz, t, map);
                }
            } else if (JSONObject.class.isAssignableFrom(filedClazz) || JSONArray.class.isAssignableFrom(filedClazz)) {
                setterObject(tClazz, fieldName, filedClazz, t, value);
            } else {
                JSONObject obj = (JSONObject) value;
                Object fieldObj = createObject(obj, filedClazz);
                setterObject(tClazz, fieldName, filedClazz, t, fieldObj);
            }
        }
        // 父类递归处理
        Class<?> superClazz = tClazz.getSuperclass();
        assignField(jsonObject, superClazz, t);
    }

    private static Class<?> getTclazz(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // getRawType()：返回原始类型Type
            return getTclazz(parameterizedType.getRawType());
        }
    }

    private static Map createMap(Type type, JSONObject jsonObject) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[1];
        Class<?> itemKlacc = getTclazz(nextType);
        boolean flag = isBaseType(itemKlacc);
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (flag) {
                map.put(key, jsonObject.opt(key));
            } else {
                Object obj = jsonObject.opt(key);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        map.put(key, obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObjectNext = (JSONObject) obj;
                        assignField(jsonObjectNext, itemKlacc, listItem);
                        map.put(key, listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    map.put(key, nextList);
                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static List createList(Type type, JSONArray jsonArray) throws Exception {
        Class<?> klacc = getTclazz(type);
        boolean flag = isBaseType(klacc);
        int length = jsonArray.length();
        List list = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[0];
        Class<?> itemKlacc = getTclazz(nextType);
        for (int i = 0; i < length; i++) {
            if (flag) {
                list.add(jsonArray.get(i));
            } else {
                Object obj = jsonArray.get(i);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        list.add(obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObject = (JSONObject) obj;
                        assignField(jsonObject, itemKlacc, listItem);
                        list.add(listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    list.add(nextList);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static Set createSet(Type type, JSONArray jsonArray) throws Exception {
        Class<?> klacc = getTclazz(type);
        boolean flag = isBaseType(klacc);
        int length = jsonArray.length();
        Set set = new HashSet();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[0];
        Class<?> itemKlacc = getTclazz(nextType);
        for (int i = 0; i < length; i++) {
            if (flag) {
                set.add(jsonArray.get(i));
            } else {
                Object obj = jsonArray.get(i);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        set.add(obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObject = (JSONObject) obj;
                        assignField(jsonObject, itemKlacc, listItem);
                        set.add(listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    set.add(nextList);
                }
            }
        }
        return set;
    }

    private static <T> void setterObject(Class<?> tClazz, String fieldName, Class<?> paramsClazz, T t, Object param) throws Exception {
        if (param instanceof Boolean && paramsClazz.isAssignableFrom(String.class)) {
            param = String.valueOf(param);
        }
        Method method = tClazz.getDeclaredMethod("set" + StringUtil.captureName(fieldName), paramsClazz);
        method.invoke(t, param);
    }
}