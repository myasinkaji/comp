package ir.component.core.engine.service;

import ir.component.core.dao.model.Media;
import ir.component.core.engine.service.exec.CommandExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * @author Mohammad Yasin Kaji
 */
@Component
public class CmdImageConverterService extends MediaConverterService {
    final Logger logger = LoggerFactory.getLogger(CmdImageConverterService.class);

    @Resource
    ConfigurableApplicationContext ctx;
    @Value("${media.numberOfThumbnails:4}")
    int numOfThumbs;
    @Value("${media.conversionCommand:}")
    String conversionCommand;
    @Resource
    private MediaService mediaService;

    @Override
    public void convert(Media media, String home, File inFile, String creator) throws Exception {
        try {
            Map<String, String> envMap = new HashMap<String, String>(envContext);
//            envMap.put("WEBAPP_HOME", uiController.resolvePath("/"));
            envMap.put("WEBAPP_HOME", "/");

            CommandExecutor ce = new CommandExecutor();
            // ce.setCommandList(Collections.singletonList(conversionCommand));
            ce.setFailover(false);

            org.springframework.core.io.Resource resource = null;
            resource = ctx.getResource(conversionCommand);


            File resFile;
            try {
                resFile = resource.getFile();
            } catch (IOException e) {
                throw new Exception("media.conversionCommand is not a real file resource: " + conversionCommand);
            }

            List<String> params = new ArrayList<String>();

            File parentInFile = inFile.getParentFile();
            // copy-rename to an ascii name
            String uuid = UUID.randomUUID().toString();
            File renamedInput = new File(parentInFile, uuid + "." + FilenameUtils.getExtension(inFile.getName()));
            FileUtils.copyFile(inFile, renamedInput);
            params.add(inFile.getCanonicalPath());

            logger.info("Convert {} ({}) into {} files.", inFile, renamedInput, numOfThumbs);
            for (int i = 1; i <= numOfThumbs; i++) {
                String thumbName = mediaService.getUniqueNameOnDisk(new Byte("0"), new Short("2"),
                        media.getCreated(), media.getKey(), "jpg", i);

                thumbName = FilenameUtils.separatorsToSystem(thumbName);
                File thumbFile = new File(home, thumbName);
                thumbFile.getParentFile().mkdirs();
                params.add(thumbFile.getCanonicalPath());
            }

            // add params to conversion command
            ce.setCommandList(Collections.singletonList(resFile.getCanonicalPath() + " " + StringUtils.join(params, " ")));

            if (creator != null) {
                envMap.put("CREATOR", creator);
            }
            ce.setEnv(envMap);

            try {
                ce.run();
            } finally {
                FileUtils.deleteQuietly(renamedInput);
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /*@Override
    public List<String> getThumbNames(Byte repos, Short siteId, Date created, Long id, String qualities) {
        List<String> ret = new ArrayList<String>();
        for (int i = 1; i <= numOfThumbs; i++) {
            ret.add(mediaService.getUniqueNameOnDisk(repos, siteId, created, id, "jpg", i));
        }
        return ret;
    }
*/
    public String getConversionCommand() {
        return conversionCommand;
    }

    public void setConversionCommand(String conversionCommand) {
        this.conversionCommand = conversionCommand;
    }


}
