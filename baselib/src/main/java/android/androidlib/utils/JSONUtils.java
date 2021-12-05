package android.androidlib.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Json解析工具类
 *
 * @author liuml
 * @time 2016-6-1 下午4:52:10
 */
public final class JSONUtils {

    public static boolean isPrintException = true;

    private JSONUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 将对象转换成Json字符串
     *
     * @param obj
     * @return
     */
    public static String toJSON(Object obj) {
        JSONStringer js = new JSONStringer();
        serialize(js, obj);
        return js.toString();
    }

    /**
     * 序列化为JSON
     *
     * @param js
     * @param o
     */
    private static void serialize(JSONStringer js, Object o) {
        if (isNull(o)) {
            try {
                js.value(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        Class<?> clazz = o.getClass();
        if (isObject(clazz)) { // 对象
            serializeObject(js, o);
        } else if (isArray(clazz)) { // 数组
            serializeArray(js, o);
        } else if (isCollection(clazz)) { // 集合
            Collection<?> collection = (Collection<?>) o;
            serializeCollect(js, collection);
        } else { // 单个值
            try {
                js.value(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 序列化数组
     *
     * @param js
     * @param array
     */
    private static void serializeArray(JSONStringer js, Object array) {
        try {
            js.array();
            for (int i = 0; i < Array.getLength(array); ++i) {
                Object o = Array.get(array, i);
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化集合
     *
     * @param js
     * @param collection
     */
    private static void serializeCollect(JSONStringer js, Collection<?> collection) {
        try {
            js.array();
            for (Object o : collection) {
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化对象
     *
     * @param js
     * @param obj
     */
    private static void serializeObject(JSONStringer js, Object obj) {
        try {
            js.object();
            Class<? extends Object> objClazz = obj.getClass();
            Method[] methods = objClazz.getDeclaredMethods();
            Field[] fields = objClazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldType = field.getType().getSimpleName();
                    String fieldGetName = parseMethodName(field.getName(), "get");
                    if (!haveMethod(methods, fieldGetName)) {
                        continue;
                    }
                    Method fieldGetMet = objClazz.getMethod(fieldGetName, new Class[]{});
                    Object fieldVal = fieldGetMet.invoke(obj, new Object[]{});
                    String result = null;
                    if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                Locale.US);
                        result = sdf.format((Date) fieldVal);

                    } else {
                        if (null != fieldVal) {
                            result = String.valueOf(fieldVal);
                        }
                    }
                    js.key(field.getName());
                    serialize(js, result);
                } catch (Exception e) {
                    continue;
                }
            }
            js.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods
     * @return boolean
     */
    public static boolean haveMethod(Method[] methods, String fieldMethod) {
        for (Method met : methods) {
            if (fieldMethod.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接某属性的 get或者set方法
     *
     * @param fieldName
     * @param methodType
     * @return
     */
    public static String parseMethodName(String fieldName, String methodType) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return methodType + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }


    /**
     * set属性的值到Bean
     *
     * @param obj
     * @param valMap
     */
    public static void setFieldValue(Object obj, Map<String, String> valMap) {
        Class<?> cls = obj.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String setMetodName = parseMethodName(field.getName(), "set");
                if (!haveMethod(methods, setMetodName)) {
                    continue;
                }
                Method fieldMethod = cls.getMethod(setMetodName, field
                        .getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldMethod.invoke(obj, value);
                    } else if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        Date temp = sdf.parse(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldMethod.invoke(obj, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldMethod.invoke(obj, temp);
                    } else {
                        System.out.println("setFieldValue not supper type:" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

    }

    /**
     * 对象转Map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> getFieldValueMap(Object obj) {
        Class<?> cls = obj.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().getSimpleName();
                String fieldGetName = parseMethodName(field.getName(), "get");
                if (!haveMethod(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
                Object fieldVal = fieldGetMet.invoke(obj, new Object[]{});
                String result = null;
                if ("Date".equals(fieldType)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    result = sdf.format((Date) fieldVal);

                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;

    }


    /**
     * 给对象的字段赋值
     *
     * @param obj
     * @param fieldSetMethod
     * @param fieldType
     * @param value
     */
    public static void setFiedlValue(Object obj, Method fieldSetMethod, String fieldType, Object value) {

        try {
            if (null != value && !"".equals(value)) {
                if ("String".equals(fieldType)) {
                    fieldSetMethod.invoke(obj, value.toString());
                } else if ("Date".equals(fieldType)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    Date temp = sdf.parse(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Integer".equals(fieldType)
                        || "int".equals(fieldType)) {
                    Integer intval = Integer.parseInt(value.toString());
                    fieldSetMethod.invoke(obj, intval);
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    Long temp = Long.parseLong(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Double".equalsIgnoreCase(fieldType)) {
                    Double temp = Double.parseDouble(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    Boolean temp = Boolean.parseBoolean(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else {
                    fieldSetMethod.invoke(obj, value);
                    //Log.e(TAG, TAG  + ">>>>setFiedlValue -> not supper type" + fieldType);
                }
            }

        } catch (Exception e) {
            //Log.e(TAG, TAG  + ">>>>>>>>>>set value error.",e);
        }

    }


    /**
     * 反序列化简单对象
     *
     * @param jo
     * @param clazz
     * @return
     * @throws JSONException
     */
    public static <T> T parseObject(JSONObject jo, Class<T> clazz) throws JSONException {
        if (clazz == null || isNull(jo)) {
            return null;
        }

        T obj = newInstance(clazz);
        if (obj == null) {
            return null;
        }
        if (isMap(clazz)) {
            setField(obj, jo);
        } else {
            // 取出bean里的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                String setMetodName = parseMethodName(f.getName(), "set");
                if (!haveMethod(methods, setMetodName)) {
                    continue;
                }
                try {
                    Method fieldMethod = clazz.getMethod(setMetodName, f.getType());
                    setField(obj, fieldMethod, f, jo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    /**
     * 反序列化简单对象
     *
     * @param jsonString
     * @param clazz
     * @return
     * @throws JSONException
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) throws JSONException {
        if (clazz == null || jsonString == null || jsonString.length() == 0) {
            return null;
        }

        JSONObject jo = null;
        jo = new JSONObject(jsonString);
        if (isNull(jo)) {
            return null;
        }

        return parseObject(jo, clazz);
    }

    /**
     * 反序列化数组对象
     *
     * @param ja
     * @param clazz
     * @return
     */
    public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
        if (clazz == null || isNull(ja)) {
            return null;
        }

        int len = ja.length();

        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, len);

        for (int i = 0; i < len; ++i) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                T o = parseObject(jo, clazz);
                array[i] = o;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    /**
     * 反序列化数组对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T[] parseArray(String jsonString, Class<T> clazz) {
        if (clazz == null || jsonString == null || jsonString.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isNull(jo)) {
            return null;
        }

        return parseArray(jo, clazz);
    }

    /**
     * 反序列化泛型集合
     *
     * @param ja
     * @param collectionClazz
     * @param genericType
     * @return
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> parseCollection(JSONArray ja, Class<?> collectionClazz,
                                                    Class<T> genericType) throws JSONException {

        if (collectionClazz == null || genericType == null || isNull(ja)) {
            return null;
        }

        Collection<T> collection = (Collection<T>) newInstance(collectionClazz);

        for (int i = 0; i < ja.length(); ++i) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                T o = parseObject(jo, genericType);
                collection.add(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return collection;
    }

    /**
     * 反序列化泛型集合
     *
     * @param jsonString
     * @param collectionClazz
     * @param genericType
     * @return
     * @throws JSONException
     */
    public static <T> Collection<T> parseCollection(String jsonString, Class<?> collectionClazz,
                                                    Class<T> genericType) throws JSONException {
        if (collectionClazz == null || genericType == null || jsonString == null
                || jsonString.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isNull(jo)) {
            return null;
        }

        return parseCollection(jo, collectionClazz, genericType);
    }

    /**
     * 根据类型创建对象
     *
     * @param clazz
     * @return
     * @throws JSONException
     */
    private static <T> T newInstance(Class<T> clazz) throws JSONException {
        if (clazz == null)
            return null;
        T obj = null;
        if (clazz.isInterface()) {
            if (clazz.equals(Map.class)) {
                obj = (T) new HashMap();
            } else if (clazz.equals(List.class)) {
                obj = (T) new ArrayList();
            } else if (clazz.equals(Set.class)) {
                obj = (T) new HashSet();
            } else {
                throw new JSONException("unknown interface: " + clazz);
            }
        } else {
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                throw new JSONException("unknown class type: " + clazz);
            }
        }
        return obj;
    }

    /**
     * 设定Map的值
     *
     * @param obj
     * @param jo
     */
    private static void setField(Object obj, JSONObject jo) {
        try {
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jo.keys();
            String key;
            Object value;
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) obj;
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jo.get(key);
                valueMap.put(key, value);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设定字段的值
     *
     * @param obj
     * @param fieldSetMethod
     * @param f
     * @param jo
     */
    private static void setField(Object obj, Method fieldSetMethod, Field f, JSONObject jo) {
        String name = f.getName();
        Class<?> clazz = f.getType();
        try {
            if (isArray(clazz)) { // 数组
                Class<?> c = clazz.getComponentType();
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object array = parseArray(ja, c);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), array);
                }
            } else if (isCollection(clazz)) { // 泛型集合
                // 获取定义的泛型类型
                Class<?> c = null;
                Type gType = f.getGenericType();
                if (gType instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) gType;
                    Type[] targs = ptype.getActualTypeArguments();
                    if (targs != null && targs.length > 0) {
                        Type t = targs[0];
                        c = (Class<?>) t;
                    }
                }

                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object o = parseCollection(ja, clazz, c);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isSingle(clazz)) { // 值类型
                Object o = jo.opt(name);
                if (o != null) {
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isObject(clazz)) { // 对象
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isList(clazz)) { // 列表
//              JSONObject j = jo.optJSONObject(name);
//              if (!isNull(j)) {
//                  Object o = parseObject(j, clazz);
//                  f.set(obj, o);
//              }
            } else {
                throw new Exception("unknow type!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设定字段的值
     *
     * @param obj
     * @param f
     * @param jo
     */
    private static void setField(Object obj, Field f, JSONObject jo) {
        String name = f.getName();
        Class<?> clazz = f.getType();
        try {
            if (isArray(clazz)) { // 数组
                Class<?> c = clazz.getComponentType();
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object array = parseArray(ja, c);
                    f.set(obj, array);
                }
            } else if (isCollection(clazz)) { // 泛型集合
                // 获取定义的泛型类型
                Class<?> c = null;
                Type gType = f.getGenericType();
                if (gType instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) gType;
                    Type[] targs = ptype.getActualTypeArguments();
                    if (targs != null && targs.length > 0) {
                        Type t = targs[0];
                        c = (Class<?>) t;
                    }
                }

                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object o = parseCollection(ja, clazz, c);
                    f.set(obj, o);
                }
            } else if (isSingle(clazz)) { // 值类型
                Object o = jo.opt(name);
                if (o != null) {
                    f.set(obj, o);
                }
            } else if (isObject(clazz)) { // 对象
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    f.set(obj, o);
                }
            } else if (isList(clazz)) { // 列表
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    f.set(obj, o);
                }
            } else {
                throw new Exception("unknow type!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    private static boolean isNull(Object obj) {
        if (obj instanceof JSONObject) {
            return JSONObject.NULL.equals(obj);
        }
        return obj == null;
    }

    /**
     * 判断是否是值类型
     *
     * @param clazz
     * @return
     */
    private static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    /**
     * 是否布尔值
     *
     * @param clazz
     * @return
     */
    public static boolean isBoolean(Class<?> clazz) {
        return (clazz != null)
                && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 是否数值
     *
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class<?> clazz) {
        return (clazz != null)
                && ((Byte.TYPE.isAssignableFrom(clazz)) || (Short.TYPE.isAssignableFrom(clazz))
                || (Integer.TYPE.isAssignableFrom(clazz))
                || (Long.TYPE.isAssignableFrom(clazz))
                || (Float.TYPE.isAssignableFrom(clazz))
                || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是字符串
     *
     * @param clazz
     * @return
     */
    public static boolean isString(Class<?> clazz) {
        return (clazz != null)
                && ((String.class.isAssignableFrom(clazz))
                || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是对象
     *
     * @param clazz
     * @return
     */
    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz);
    }

    /**
     * 判断是否是数组
     *
     * @param clazz
     * @return
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * 判断是否是集合
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是Map
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是列表
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

    //解析JsonArray
    public static ArrayList<HashMap<String, Object>> jsonParseList(String json) {
        ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
        if (!TextUtils.isEmpty(json)) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
                //解析JsonArray
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonStr = jsonArray.optJSONObject(j);
                    Map<String, Object> maps = new HashMap<String, Object>();
                    for (int k = 0; k < jsonStr.length(); k++) {
                        String key = jsonStr.names().optString(k);
                        String value = getTextString(jsonStr.optString(key, ""));
                        maps.put(key, value);
                    }
                    mList.add((HashMap<String, Object>) maps);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return mList;
    }

    /*获取TextView现实的数据*/
    public static String getTextString(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("null")) {
            return str;
        } else {
            return "";
        }
    }




    /**
     * get Long from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getLong(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getLong(String)}</li>
     * </ul>
     */
    public static Long getLong(JSONObject jsonObject, String key,
                               Long defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Long from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return {@link JSONUtils#getLong(JSONObject, String, Long)}</li>
     * </ul>
     */
    public static Long getLong(String jsonData, String key, Long defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getLong(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getLong(JSONObject, String, Long)
     */
    public static long getLong(JSONObject jsonObject, String key,
                               long defaultValue) {
        return getLong(jsonObject, key, (Long) defaultValue);
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getLong(String, String, Long)
     */
    public static long getLong(String jsonData, String key, long defaultValue) {
        return getLong(jsonData, key, (Long) defaultValue);
    }

    /**
     * get Int from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getInt(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getInt(String)}</li>
     * </ul>
     */
    public static Integer getInt(JSONObject jsonObject, String key,
                                 Integer defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Int from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return {@link JSONUtils#getInt(JSONObject, String, Integer)}</li>
     * </ul>
     */
    public static Integer getInt(String jsonData, String key,
                                 Integer defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getInt(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getInt(JSONObject, String, Integer)
     */
    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        return getInt(jsonObject, key, (Integer) defaultValue);
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getInt(String, String, Integer)
     */
    public static int getInt(String jsonData, String key, int defaultValue) {
        return getInt(jsonData, key, (Integer) defaultValue);
    }

    /**
     * get Double from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getDouble(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getDouble(String)}</li>
     * </ul>
     */
    public static Double getDouble(JSONObject jsonObject, String key,
                                   Double defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Double from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getDouble(JSONObject, String, Double)}</li>
     * </ul>
     */
    public static Double getDouble(String jsonData, String key,
                                   Double defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getDouble(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getDouble(JSONObject, String, Double)
     */
    public static double getDouble(JSONObject jsonObject, String key,
                                   double defaultValue) {
        return getDouble(jsonObject, key, (Double) defaultValue);
    }

    /**
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getDouble(String, String, Double)
     */
    public static double getDouble(String jsonData, String key,
                                   double defaultValue) {
        return getDouble(jsonData, key, (Double) defaultValue);
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getString(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getString(String)}</li>
     * </ul>
     */
    public static String getString(JSONObject jsonObject, String key,
                                   String defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getString(JSONObject, String, String)}</li>
     * </ul>
     */
    public static String getString(String jsonData, String key,
                                   String defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getString(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if keyArray is null or empty, return defaultValue</li>
     * <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by
     * recursion, return it. if anyone is null, return directly</li>
     * </ul>
     */
    public static String getStringCascade(JSONObject jsonObject,
                                          String defaultValue, String... keyArray) {
        if (jsonObject == null || keyArray == null || keyArray.length == 0) {
            return defaultValue;
        }

        String data = jsonObject.toString();
        for (String key : keyArray) {
            data = getStringCascade(data, key, defaultValue);
            if (data == null) {
                return defaultValue;
            }
        }
        return data;
    }

    /**
     * get String from jsonData
     *
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     * <li>if jsonData is null, return defaultValue</li>
     * <li>if keyArray is null or empty, return defaultValue</li>
     * <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by
     * recursion, return it. if anyone is null, return directly</li>
     * </ul>
     */
    public static String getStringCascade(String jsonData, String defaultValue,
                                          String... keyArray) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        String data = jsonData;
        for (String key : keyArray) {
            data = getString(data, key, defaultValue);
            if (data == null) {
                return defaultValue;
            }
        }
        return data;
    }

    /**
     * get String array from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>if {@link JSONArray#getString(int)} exception, return
     * defaultValue</li>
     * <li>return string array</li>
     * </ul>
     */
    public static String[] getStringArray(JSONObject jsonObject, String key,
                                          String[] defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                String[] value = new String[statusArray.length()];
                for (int i = 0; i < statusArray.length(); i++) {
                    value[i] = statusArray.getString(i);
                }
                return value;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * get String array from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getStringArray(JSONObject, String, String[])}</li>
     * </ul>
     */
    public static String[] getStringArray(String jsonData, String key,
                                          String[] defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringArray(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String list from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>if {@link JSONArray#getString(int)} exception, return
     * defaultValue</li>
     * <li>return string array</li>
     * </ul>
     */
    public static List<String> getStringList(JSONObject jsonObject, String key,
                                             List<String> defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < statusArray.length(); i++) {
                    list.add(statusArray.getString(i));
                }
                return list;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * get String list from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getStringList(JSONObject, String, List)}</li>
     * </ul>
     */
    public static List<String> getStringList(String jsonData, String key,
                                             List<String> defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getStringList(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONObject(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getJSONObject(String)}</li>
     * </ul>
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String key,
                                           JSONObject defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonData is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getJSONObject(JSONObject, String, JSONObject)}</li>
     * </ul>
     */
    public static JSONObject getJSONObject(String jsonData, String key,
                                           JSONObject defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONObject(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONObject from jsonObject
     *
     * @param jsonObject
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if keyArray is null or empty, return defaultValue</li>
     * <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by
     * recursion, return it. if anyone is null, return directly</li>
     * </ul>
     */
    public static JSONObject getJSONObjectCascade(JSONObject jsonObject,
                                                  JSONObject defaultValue, String... keyArray) {
        if (jsonObject == null || keyArray == null || keyArray.length == 0) {
            return defaultValue;
        }

        JSONObject js = jsonObject;
        for (String key : keyArray) {
            js = getJSONObject(js, key, defaultValue);
            if (js == null) {
                return defaultValue;
            }
        }
        return js;
    }

    /**
     * get JSONObject from jsonData
     *
     * @param jsonData
     * @param defaultValue
     * @param keyArray
     * @return <ul>
     * <li>if jsonData is null, return defaultValue</li>
     * <li>if keyArray is null or empty, return defaultValue</li>
     * <li>get {@link #getJSONObject(JSONObject, String, JSONObject)} by
     * recursion, return it. if anyone is null, return directly</li>
     * </ul>
     */
    public static JSONObject getJSONObjectCascade(String jsonData,
                                                  JSONObject defaultValue, String... keyArray) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONObjectCascade(jsonObject, defaultValue, keyArray);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONArray from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getJSONArray(String)}</li>
     * </ul>
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String key,
                                         JSONArray defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONArray from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#( JSONObject , String , JSONObject )}</li>
     * </ul>
     */
    public static JSONArray getJSONArray(String jsonData, String key,
                                         JSONArray defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getJSONArray(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Boolean from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>return {@link JSONObject#getBoolean(String)}</li>
     * </ul>
     */
    public static boolean getBoolean(JSONObject jsonObject, String key,
                                     Boolean defaultValue) {
        if (jsonObject == null || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Boolean from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getBoolean(JSONObject, String, Boolean)}</li>
     * </ul>
     */
    public static boolean getBoolean(String jsonData, String key,
                                     Boolean defaultValue) {
        if (StringUtils.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getBoolean(jsonObject, key, defaultValue);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get map from jsonObject.
     *
     * @param jsonObject key-value pairs json
     * @param key
     * @return <ul>
     * <li>if jsonObject is null, return null</li>
     * <li>return {@link JSONUtils#parseKeyAndValueToMap(String)}</li>
     * </ul>
     */
    public static Map<String, String> getMap(JSONObject jsonObject, String key) {
        return JSONUtils.parseKeyAndValueToMap(JSONUtils.getString(jsonObject,
                key, null));
    }

    /**
     * get map from jsonData.
     *
     * @param jsonData key-value pairs string
     * @param key
     * @return <ul>
     * <li>if jsonData is null, return null</li>
     * <li>if jsonData length is 0, return empty map</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return null</li>
     * <li>return {@link JSONUtils#getMap(JSONObject, String)}</li>
     * </ul>
     */
    public static Map<String, String> getMap(String jsonData, String key) {

        if (jsonData == null) {
            return null;
        }
        if (jsonData.length() == 0) {
            return new HashMap<String, String>();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getMap(jsonObject, key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception,
     * put empty value
     *
     * @param sourceObj key-value pairs json
     * @return <ul>
     * <li>if sourceObj is null, return null</li>
     * <li>else parse entry one by one</li>
     * </ul>
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if (sourceObj == null) {
            return null;
        }

        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        for (Iterator iter = sourceObj.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            keyAndValueMap.put(key, getString(sourceObj, key, ""));
        }
        return keyAndValueMap;
    }

    /**
     * parse key-value pairs to map. ignore empty key, if getValue exception,
     * put empty value
     *
     * @param source key-value pairs json
     * @return <ul>
     * <li>if source is null or source's length is 0, return empty map</li>
     * <li>if source {@link JSONObject#JSONObject(String)} exception,
     * return null</li>
     * <li>return {@link JSONUtils#parseKeyAndValueToMap(JSONObject)}</li>
     * </ul>
     */
    public static Map<String, String> parseKeyAndValueToMap(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(source);
            return parseKeyAndValueToMap(jsonObject);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
    }
}