package ir.magfa.sdk.farhadi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import ir.magfa.sdk.dto.DocumentDTO;
import ir.magfa.sdk.dto.KartableTaskDTO;
//import ir.magfa.sdk.dto.PermRequest;
import org.kie.server.api.model.definition.QueryDefinition;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.QueryServicesClient;


/**
 * Created by Mostafa.ّFarhadi on 11/28/2017.
 */
public class JbpmClientUtils {
/*
    public static Map<String,Object> fillParam(PermRequest permRequest){
        Gson gson = new Gson();
        String jsonParam = gson.toJson(permRequest);
        java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> paramMap= gson.fromJson(jsonParam, mapType );
//        paramMap.put("decision", false);
        return paramMap;
    }
*/


    public static Map<String,Object> json(Object object){
        Gson gson = new Gson();
        String jsonParam = gson.toJson(object);
        java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> paramMap= gson.fromJson(jsonParam, mapType );
//        paramMap.put("decision", false);
        return paramMap;
    }


    public static Map<String,Object> fillParam(Object obj) throws IllegalAccessException {
        Gson gson = new Gson();
        Field[] fieldsObject = obj.getClass().getDeclaredFields();
        DocumentDTO documentDTO = (DocumentDTO) getInstanceRuntime();
        Field[] fieldsDocuments = documentDTO.getClass().getDeclaredFields();
        int size = fieldsDocuments.length;
        int i = 0;
        for (Field field : fieldsObject) {
            if (i<=size){
                field.setAccessible(true);
                Field docField = (Field) fieldsDocuments[i];
                docField.setAccessible(true);
                if(field!=null){
                    try {
                        docField.set(documentDTO,field.get(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            i++;
        }
        String jsonParam = gson.toJson(documentDTO);
        java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> paramMap= gson.fromJson(jsonParam, mapType );
        return paramMap;

    }
    public static  Object getInstanceRuntime() {
        Object result = null;
        Properties properties = new Properties();
        try {

            Class c = Class.forName("ir.magfa.sdk.dto.DocumentDTO");
            result = c.newInstance();
        } catch (Exception e) {
            e.getStackTrace();}

        return result;
    }


//    [[evaluation, 210.0, 325.0, Self Evaluation, hadi, null, Reserved]]

    public static void toJson(List<String> list){
        String[] strArray = new String[6];
        List<KartableTaskDTO> kartablList = new ArrayList<>();
        KartableTaskDTO KartableTaskDTO = null;
       for (int i=0;i<list.size();i++){
           for (int j=0;j<=6;j++){
               KartableTaskDTO = new KartableTaskDTO();
               strArray = list.get(j).split(",");
                   KartableTaskDTO.setProcessId(strArray[0]);
                   KartableTaskDTO.setProcessInstanceId(strArray[1]);
                   KartableTaskDTO.setTaskId(strArray[2]);
                   KartableTaskDTO.setTaskName(strArray[3]);
                   KartableTaskDTO.setActualOwnerId(strArray[4]);
                   KartableTaskDTO.setTaskInitiatorId(strArray[5]);
                   KartableTaskDTO.setStatus(strArray[6]);
                   kartablList.add(KartableTaskDTO);
           }
       }
        for (KartableTaskDTO kartable : kartablList) {
            System.out.print(kartable.getActualOwnerId());
        }

//        Gson gson = new Gson();
//        String str = gson.toJson(list);
        System.out.println(list);

    }

    public static List<String> listListToCurentList(List<ArrayList<String>> listOfLists)
    {
        List<String> returnList = listOfLists.get(0);

        for (int i = 1; i < listOfLists.size(); i++) {

            List<String> listFour = new ArrayList<String>();

            for (int j = 0; j < returnList.size(); j++) {
                ArrayList<String> tempList = listOfLists.get(i);
                for (int k = 0; k < tempList.size(); k++) {
                    listFour.add(returnList.get(j) + " " + tempList.get(k));
                }
            }

            returnList = new ArrayList();

            for (int m = 0; m < listFour.size(); m++) {
                returnList.add(listFour.get(m));
            }
        }

        return returnList;
    }


    public static  List<KartableTaskDTO> printNextUserInfo(KieServicesClient kieServicesClient, Long processId, int state) {
    	String magfaQueryName = "KartableMagfa" + processId;
        List<KartableTaskDTO> list = new ArrayList<>();
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
//        test(queryClient);
        if (1 == state) {
            QueryDefinition query = new QueryDefinition();
            query.setName(magfaQueryName);
            query.setSource("java:jboss/datasources/ExampleDS");
            query.setExpression("select t.processid ,t.processinstanceid ,t.id,t.name,t.actualowner_id,taskinitiator_id,t.status,t.formname" + " " +
                    " from task t where t.processinstanceid =" + processId);
            query.setTarget("TASK");
            queryClient.registerQuery(query);
        }

        try {
            List tasks = queryClient.query(magfaQueryName, "RawList", 0, 10, void.class);
            KartableTaskDTO KartableTaskDTO = null;
           
            for (int i = 0; i < tasks.size(); i++) {
                KartableTaskDTO = new KartableTaskDTO();
                ArrayList<Object> innerList = (ArrayList<Object>) tasks.get(i);
                KartableTaskDTO.setProcessId(innerList.get(0) + "");
                KartableTaskDTO.setProcessInstanceId(innerList.get(1) + "");
                KartableTaskDTO.setTaskId(innerList.get(2) + "");
                KartableTaskDTO.setTaskName(innerList.get(3) + "");
                KartableTaskDTO.setActualOwnerId(innerList.get(4) + "");
                KartableTaskDTO.setStatus(innerList.get(6) + "");
                KartableTaskDTO.setPortletName(innerList.get(7) + "");
                list.add(KartableTaskDTO);
            }
            Gson gson = new Gson();
//        String str = gson.toJson(list);
//        System.out.println(str);
           // list.forEach(item -> System.out.println(item));

           
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    private static void test(QueryServicesClient queryClient) {
        String QUERY_NAME = "Kaji12";
// building the query
        QueryDefinition queryDefinition = new QueryDefinition();
        queryDefinition.setName(QUERY_NAME);

        queryDefinition.setExpression("select t.id from Task t");
        queryDefinition.setSource("java:jboss/datasources/ExampleDS");
        queryDefinition.setTarget("TASK");

//        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);

// two queries cannot have the same name
        queryClient.unregisterQuery(QUERY_NAME);

// register the query
        queryClient.registerQuery(queryDefinition);

// execute the query with parameters: query name, mapping type (to map the fields to an object), page number, page size and return type

        Long id = queryClient.query(QUERY_NAME, QueryServicesClient.QUERY_MAP_TASK, 0, 100, Long.class).get(0);
//        Long id = instance.getId();
        System.out.println(id);
    }

    public static Long readProccessIDFromFile(){


        String fileName = "C:\\prs\\prs.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return Long.parseLong(line);
    }
}
