package utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * nickolay, 28.05.15.
 */
public class Bundle {
    private HashMap<String, Object> mHashMap = new HashMap<>();

    public boolean has(String key) {
        return mHashMap.containsKey(key);
    }

    public void clear() {
        mHashMap.clear();
    }

    public void putString(String key, String value) {
        mHashMap.put(key, value);
    }

    public String getString(String key) {
        return (String) mHashMap.get(key);
    }

    public void putSerializable(String key, Serializable object) {
        mHashMap.put(key, object);
    }

    public Serializable getSerializable(String key) {
        return (Serializable) mHashMap.get(key);
    }

    public void putInteger(String key, Integer value) {
        mHashMap.put(key, value);
    }

    public Integer getInteger(String key) {
        return (Integer) mHashMap.get(key);
    }
}
