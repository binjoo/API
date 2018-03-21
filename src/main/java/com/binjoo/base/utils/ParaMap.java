package com.binjoo.base.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ParaMap extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = -7061525378628638822L;

    public ParaMap() {
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return super.entrySet();
    }

    public Object get(String key) {
        return super.get(key);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        String v = defaultValue;
        if (containsKey(key)) {
            v = String.valueOf(get(key));
        }
        return v;
    }

    public Double getDouble(String key) {
        if (containsKey(key)) {
            String v = getString(key);
            Double d = Double.valueOf(Double.parseDouble(v));
            return d;
        }
        return null;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Integer v = getInteger(key);
        if (v == null) {
            return defaultValue;
        }
        return v.intValue();
    }

    public Integer getInteger(String key) {
        if (containsKey(key)) {
            String v = getString(key);
            int i = Integer.parseInt(v);
            return Integer.valueOf(i);
        }
        return null;
    }

    public List getList(String key) {
        Object obj = get(key);
        return (List) obj;
    }

    public boolean getBool(String key) {
        return getBool(key, false);
    }

    public boolean getBool(String key, boolean defaultValue) {
        Boolean b = getBoolean(key);
        if (b == null) {
            return defaultValue;
        }
        return b.booleanValue();
    }

    public Boolean getBoolean(String key) {
        if (containsKey(key)) {
            String v = getString(key);
            boolean b = Boolean.parseBoolean(v);
            return Boolean.valueOf(b);
        }
        return null;
    }

    public String toString() {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(this));
        return jsonObject.toString();
    }
}
