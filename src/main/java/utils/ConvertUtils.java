package utils;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.MutableCapabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConvertUtils {

    public static MutableCapabilities mapToCapabilities(Map<String, Object> map){
        MutableCapabilities mutableCapabilities = new MutableCapabilities();
        map.keySet().forEach(key -> mutableCapabilities.setCapability(key, map.get(key)));
        return mutableCapabilities;
    }

    public static Map<String, Object> getJsonObject(JSONObject jsonObj) {
        Map<String, Object> map = new HashMap<>();
        for (Object key : jsonObj.keySet()) {
            String keyStr = (String) key;
            Object keyValue = jsonObj.get(keyStr);
            if (keyValue instanceof JSONObject) {
                //log.info("For key " + keyStr + " was found jsonObject " + keyValue);
                keyValue = getJsonObject((JSONObject) keyValue);
                //
            } else if (keyValue instanceof JSONArray) {
                //log.info("For key " + keyStr + " was found jsonArray " + keyValue);
                keyValue = jsonArrayToList((JSONArray) keyValue);
            }
            map.put(keyStr, keyValue);
        }
        return map;
    }

    private static List<Object> jsonArrayToList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (Object value : array) {
            if (value instanceof JSONArray) {
                value = jsonArrayToList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = getJsonObject((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
