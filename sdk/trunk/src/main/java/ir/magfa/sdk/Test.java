package ir.magfa.sdk;

import ir.magfa.sdk.dto.ChainDTO;
import ir.magfa.sdk.dto.KartableTaskDTO;
import ir.magfa.sdk.dto.Note;
//import ir.magfa.sdk.dto.PermRequest;
import ir.magfa.sdk.dto.RequestDTO;
import ir.magfa.sdk.utils.KieClientConstant;
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
public class Test
{


    public static void main(String[] args) throws IllegalAccessException, IOException {
        String username = "hadi";
        String password = "qwer1234";

        List<KartableTaskDTO> result = null;
        MagfaKieSDK magfaKieSDK = new MagfaKieSDKImpl();
    /*    Note note = new Note();
        note.setId(0l);
        note.setEmployee("hadi");
        note.setReason("speak english");
*/
       /* ChainDTO chainDTO = new ChainDTO();
        chainDTO.setId(1);
        chainDTO.setProductName("KajiDTO");
        chainDTO.setPrice(12);
        chainDTO.setProductCode(14);*/

/*        PermRequest request = new PermRequest();

        request.setId(66666);
        request.setStudent("from java code");*/

//        result= magfaKieSDK.findAllTask(1, "http://172.16.9.50:8230/kie-server/services/rest/server", note, "kieserver", "kieserver1!");
        RequestDTO requestDTO = new RequestDTO("kaji employee", "kaji reason",
                "kaji performance", "kaji portletName", "kaji documentId");

        KieServicesClient client= magfaKieSDK.initConfiguration(KieClientConstant.SERVICE_URL, username, password);
        QueryServicesClient queryClient = magfaKieSDK.getQueryClient(client);
        ProcessServicesClient processClient = magfaKieSDK.getProcessServicesClient(client);
        UserTaskServicesClient taskClient = magfaKieSDK.getUserTaskServicesClient(client);
        KieContainerResourceList containers = (KieContainerResourceList)client.listContainers().getResult();
        magfaKieSDK.getAllAvailableProcess(client);
        result = magfaKieSDK.stateMachine(2,"kieserver",client,taskClient,queryClient,processClient,containers,requestDTO);

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
