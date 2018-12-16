package ir.component.core.engine.service;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import ir.component.core.dao.model.Media;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Mohammad Yasin Kaji
 */
@Service
public class MediaService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    @Qualifier("cmdImageConverterService")
    private MediaConverterService imageConverterService;


    @Resource
    private RepositoryService repositoryService;


    public void store(Media media, InputStream inps) throws Exception {
        //TODO setting fake id for media
        media.setKey(UUID.randomUUID().toString());
        String uniqueName = getUniqueNameOnDisk(media);
        uniqueName = FilenameUtils.separatorsToSystem(uniqueName);

        File file = repositoryService.store(uniqueName, inps);

        media.setFile(file);

        // calls #save(Media) which adds to index
        convertMedia(media, file);

    }


    @Transactional
    public void convertMedia(Media media, File file) throws Exception {
        if (file == null || !file.exists()) {
            logger.error("File not found: {} for media: {}", file, media);
            return;
        }

        imageConverterService.convert(media, repositoryService.getHome(), file, "Photographer");

    }

    public String getUniqueNameOnDisk(Media media) {
        return getUniqueNameOnDisk(media, 0);
    }

    public String getUniqueNameOnDisk(Media media, int size) {
        String ext = FilenameUtils.getExtension(media.getOriginalFilename());
        return getUniqueNameOnDisk(new Byte("0"), new Short("2"), media.getCreated(), media.getKey(), size <= 0 ? ext : "jpg", size);
    }

    public String getUniqueNameOnDisk(Byte repos, Short siteId, Date created, String key, String extension, Integer size) {
        return getUniqueNameOnDisk(repos, siteId, created, key, extension, size, null);
    }

    public String getUniqueNameOnDisk(Byte repos, Short siteId, Date created, String key, String extension, Integer size,
                                      String qualityPostfix) {
//        MediaRepos mr = cacheService.getMediaReposMap().get(repos.intValue());
        String un = getUniqueName(repos, siteId, created, key, extension, size == null ? "1" : size.toString(), qualityPostfix);
//        un = (mr == null || StringUtils.isEmpty(mr.getDiskPath()) ? "" : (mr.getDiskPath() + "/")) + un;
        return un;
    }

    public String getUniqueName(Byte repos, Short siteId, Date created, String key, String extension, String size, String qualityPostfix) {
        if (key == null || created == null) {
            throw new NullPointerException("Media ID or fileName is null.");
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        String ret = String.format("%s/%s/%s%s", df.format(created), size == null ? "1" : size, key,
                StringUtils.isNoneEmpty(qualityPostfix) ? qualityPostfix : "");

        return ret + (ret.endsWith(".") || StringUtils.isEmpty(extension) ? "" : ".")
                + (StringUtils.isNoneEmpty(extension) ? extension.toLowerCase() : "");
    }

}
