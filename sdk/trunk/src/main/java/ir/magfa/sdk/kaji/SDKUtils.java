package ir.magfa.sdk.kaji;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ir.magfa.sdk.dto.DocumentDTO;
import ir.magfa.sdk.dto.KartableTaskDTO;
import org.kie.server.api.model.definition.QueryDefinition;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.QueryServicesClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
