package com.yxy.lib.base.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonUtil {

    public static <T> T toObjectByJsonElement(JsonElement result, Class<T> clazz) {
        if (clazz == null || result == null || result.isJsonNull()) {
            return null;
        }

        try {
            Gson gson = new Gson();
            return gson.fromJson(result, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return clazz.newInstance();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
    public static <T> T toObjectByString(String result, Class<T> clazz) {
        if (clazz == null || TextUtils.isEmpty(result)) {
            return null;
        }

        try {
            Gson gson = new Gson();
            return gson.fromJson(result, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return clazz.newInstance();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static <T> List<T> toObjectList(JsonElement json, Class<T> clazz) {
        if (clazz == null || json == null || json.isJsonNull()) {
            return null;
        }

        try {
            List<T> result = new ArrayList<T>();
            Gson gson = new Gson();
            if (json.isJsonArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                Iterator iter = jsonArray.iterator();
                while (iter.hasNext()) {
                    JsonObject obj = (JsonObject) iter.next();
                    T objClazz = null;
                    try {
                        objClazz = gson.fromJson(obj, clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result.add(objClazz);
                }
            } else {
                result.add(gson.fromJson(json, clazz));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> toObjectList(String jsonArray, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        try {
            JSONArray array = new JSONArray(jsonArray);
            for (int i = 0; i < array.length(); i++) {
                T objClazz = null;
                try {
                    objClazz = gson.fromJson(array.getString(i), clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.add(objClazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static Object jsonParseString(String json, Class<?> cl) {
        Gson gson = new Gson();
        return gson.fromJson(json, cl);
    }

    public static List<Map<String, Object>> getList(String jsonString) {
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MapToJson(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object vObject = entry.getValue();
            try {
                jsonObject.put((String) key, vObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return jsonObject.toString();
    }

    public static String MapToJsonArray(Map<String, Object> map) {
        JSONArray jsonArray = new JSONArray();
        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object vObject = entry.getValue();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put((String) key, vObject);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return jsonArray.toString();
    }

    public static JSONArray MapToJsonArray_(Map<String, Object> map) {
        JSONArray jsonArray = new JSONArray();
        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object vObject = entry.getValue();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put((String) key, vObject);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return jsonArray;
    }

    public static String ListToJson(List<Map<String, Object>> list) {
        JSONArray jsonArray = new JSONArray();

        for (Map<String, Object> map : list) {
            Iterator<?> iter = map.entrySet().iterator();
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object vObject = entry.getValue();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put((String) key, vObject);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        return jsonArray.toString();
    }


    public static <T> JSONObject generateJSONObject(T data) {
        JSONObject jsonObject = new JSONObject();
        Field f[] = data.getClass().getDeclaredFields();
        for (Field field : f) {
            if ("CREATOR".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            try {
                jsonObject.put(field.getName(), field.get(data));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static <T> JSONArray generateJSONArray(List<T> list) {
        JSONArray array = new JSONArray();
        for (T time : list) {
            array.put(JsonUtil.generateJSONObject(time));
        }
        return array;
    }

    static Gson gson = new Gson();

    public static <T> T toObject(String result, Class<T> clazz) {
        if (clazz == null || result == null || result.length() == 0) {
            return null;
        }

        try {
            return gson.fromJson(result, clazz);
        } catch (Exception e) {
            try {
                return clazz.newInstance();
            } catch (IllegalAccessException e1) {
            } catch (InstantiationException e1) {
            }
        }
        return null;
    }
}
