package ir.magfa.sdk.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 *
 * @author Mohammad Yasin Kaji
 */
public class SDKUtils {

    public static Map<String,Object> json(Object object){
        Gson gson = new Gson();
        String jsonParam = gson.toJson(object);
        java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> paramMap= gson.fromJson(jsonParam, mapType );
//        paramMap.put("decision", false);
        return paramMap;
    }

}
