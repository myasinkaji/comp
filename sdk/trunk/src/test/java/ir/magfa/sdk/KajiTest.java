package ir.magfa.sdk;

import ir.magfa.sdk.dto.ChainDTO;
import ir.magfa.sdk.dto.KartableTaskDTO;
import ir.magfa.sdk.farhadi.KieClientConstant;
import ir.magfa.sdk.farhadi.MagfaKieSDK;
import ir.magfa.sdk.farhadi.MagfaKieSDKImpl;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mostafa on 6/17/2018.
 */
public class KajiTest
{


    public static void main(String[] args) throws IllegalAccessException, IOException {
        String username = "kieserver";
        String password = "kieserver1!";

        List<KartableTaskDTO> result = null;
        MagfaKieSDK magfaKieSDK = new MagfaKieSDKImpl();
       /* Note note = new Note();
        note.setId(0l);
        note.setEmployee("hadi");
        note.setReason("speak english");*/

        ChainDTO chainDTO = new ChainDTO();
        chainDTO.setId(1);
        chainDTO.setProductName("KajiDTO");
        chainDTO.setPrice(12);
        chainDTO.setProductCode(14);

//        result= magfaKieSDK.findAllTask(1, "http://172.16.9.50:8230/kie-server/services/rest/server", note, "kieserver", "kieserver1!");
        KieServicesClient client= magfaKieSDK.initConfiguration(KieClientConstant.SERVICE_URL, username, password);
        QueryServicesClient queryClient = magfaKieSDK.getQueryClient(client);
        ProcessServicesClient processClient = magfaKieSDK.getProcessServicesClient(client);
        UserTaskServicesClient taskClient = magfaKieSDK.getUserTaskServicesClient(client);
        KieContainerResourceList containers = (KieContainerResourceList)client.listContainers().getResult();
        magfaKieSDK.getAllAvailableProcess(client);
//        result = magfaKieSDK.stateMachine(1,username,client,taskClient,queryClient,processClient,containers,chainDTO);

        for (KartableTaskDTO kartableTaskDTO : result){
            System.out.println(kartableTaskDTO.getTaskId());
        }
    }
//
//
//    public void ch1(){
//        Gson gson = new Gson();
//    }

}
