package ir.component.web.controller;

import ir.component.core.dao.model.AbstractAdvertisement;
import ir.component.core.dao.model.Advertisement;
import ir.component.core.dao.model.Media;
import ir.component.core.engine.AdvertisementDao;
import ir.component.core.engine.service.MediaService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.util.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.*;

/**
 * @author Zahra Afsharinia
 */
@Controller
@Scope("session")
public class AdvertisementController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Advertisement advertisement = new Advertisement();

    @Resource
    private MediaService mediaService;

    @Resource
    private AdvertisementDao advertisementDao;
    @Value("${dissallowedFileExtensions:com,exe,bat,cmd,html,htm,body}")
    private String dissallowedFileExtensionsStr;
    private Set<String> dissallowedFileExtensions = Collections.emptySet();

    public AdvertisementController() {
    }

    @PostConstruct
    public void postConstruct() {
        if (dissallowedFileExtensionsStr != null) {
            dissallowedFileExtensions = new HashSet<>(
                    Arrays.asList(StringUtils.split(dissallowedFileExtensionsStr.toLowerCase(), ',')));
        }
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public boolean validate() {
        if (StringUtils.isEmpty(advertisement.getTitle())) {
            FacesContext.getCurrentInstance().addMessage("clientId",
                    MessageFactory.getMessage(UIInput.REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR,
                            new Object[]{"title"/*uiController.localize("title") */}));
            FacesContext.getCurrentInstance().validationFailed();
            return false;
        }
        return true;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String s = String.format("UploadedFile [fileName=%s, size=%s, contentType=%s]", file.getFileName(),
                    file.getSize(), file.getContentType());
            logger.info("handleFileUpload: {}", s);


            Media media = store(file);

            List<Media> mediaList = new ArrayList<Media>();
            mediaList.add(media);
            advertisement.setMediaList(mediaList);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("clientId",
                    MessageFactory.getMessage(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME, FacesMessage.SEVERITY_ERROR,
                            new Object[]{e.getMessage()/*uiController.localize("title") */}));
        }
    }


    private Media store(UploadedFile file) throws Exception {
        String normalizedFilename = UnicodeNormalizer.normalizeFileName(FilenameUtils.getName(file.getFileName()));
        Media media = new Media();

        media.setOriginalFilename(normalizedFilename);
        String ext = FilenameUtils.getExtension(media.getOriginalFilename());

        validateFileExtension(normalizedFilename, ext);
        media.setOriginalFormat(ext);
        media.setOriginalSize((int) file.getSize());

        media.setFilename(file.getFileName());
        media.setCreated(new Date());
        InputStream inps = null;
        if (file != null) {
            inps = file.getInputstream();
        }

        mediaService.store(media, inps);

        return media;
    }

    private void validateFileExtension(String filename, String extension) throws ComponentSecurityException {
        if (dissallowedFileExtensions != null && dissallowedFileExtensions.contains(StringUtils.lowerCase(extension))) {
            throw new ComponentSecurityException("File extension is not allowed: " + extension);
        }
    }

    public void save() {
        if (CollectionUtils.isNotEmpty(advertisement.getMediaList()))
            for (Media media : advertisement.getMediaList())
                media.setAdvertisement(advertisement);

        advertisementDao.persist(advertisement);
        advertisement = new Advertisement();
    }

    public List<AbstractAdvertisement> getAllAdvertisement() {
        return advertisementDao.allAdvertisements("Advertisement");
    }
}
