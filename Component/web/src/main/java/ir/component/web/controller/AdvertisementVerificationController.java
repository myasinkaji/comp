package ir.component.web.controller;

import ir.component.core.dao.model.*;
import ir.component.core.engine.AdvertisementDao;
import ir.component.core.engine.service.MediaService;
import ir.component.web.service.UiUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zahra Afsharinia
 */
@Controller
@Scope("session")
public class AdvertisementVerificationController {


    @Resource
    private MediaService mediaService;

    @Resource
    private AdvertisementDao advertisementDao;

    private Map<Advertisement, Boolean> dirtyAdvertisements = new HashMap<Advertisement, Boolean>();


    public void accept(Advertisement advertisement) {
        dirtyAdvertisements.put(advertisement, true);
    }

    public void reject(Advertisement advertisement) {
        dirtyAdvertisements.put(advertisement, false);
    }

    public void save() throws IOException {
        for (Map.Entry<Advertisement, Boolean> entry : dirtyAdvertisements.entrySet()) {
            Advertisement advertisementItem = entry.getKey();
            if (entry.getValue()) {
                AcceptedAdvertisement acceptedAdvertisement = new AcceptedAdvertisement();
                acceptedAdvertisement.setTitle(advertisementItem.getTitle());
                acceptedAdvertisement.setGuild(advertisementItem.getGuild());
                acceptedAdvertisement.setFromValidityDate(advertisementItem.getFromValidityDate());
                acceptedAdvertisement.setToValidityDate(advertisementItem.getToValidityDate());
                acceptedAdvertisement.setPrice(advertisementItem.getPrice());
                acceptedAdvertisement.setPurchaseDiscount(advertisementItem.getPurchaseDiscount());
                acceptedAdvertisement.setSaleCommission(advertisementItem.getSaleCommission());
                acceptedAdvertisement.setDescription(advertisementItem.getDescription());
//                acceptedAdvertisement.setMediaList(advertisementItem.getMediaList());
                advertisementDao.persist(acceptedAdvertisement);
            } else {
                RejectedAdvertisement rejectedAdvertisement = new RejectedAdvertisement();
                rejectedAdvertisement.setTitle(advertisementItem.getTitle());
                rejectedAdvertisement.setGuild(advertisementItem.getGuild());
                rejectedAdvertisement.setFromValidityDate(advertisementItem.getFromValidityDate());
                rejectedAdvertisement.setToValidityDate(advertisementItem.getToValidityDate());
                rejectedAdvertisement.setPrice(advertisementItem.getPrice());
                rejectedAdvertisement.setPurchaseDiscount(advertisementItem.getPurchaseDiscount());
                rejectedAdvertisement.setSaleCommission(advertisementItem.getSaleCommission());
                rejectedAdvertisement.setDescription(advertisementItem.getDescription());
//                rejectedAdvertisement.setMediaList(advertisementItem.getMediaList());
                advertisementDao.persist(rejectedAdvertisement);
            }
//            advertisementDao.
            advertisementDao.removeDetached(advertisementItem);
        }
        UiUtils.refresh();
    }

    public List<AbstractAdvertisement> getAllAdvertisement() {
        return advertisementDao.allAdvertisements("Advertisement");
    }


    public String mediaUrl(AbstractAdvertisement advertisement) {
        Advertisement adv = (Advertisement) advertisement;
        if (CollectionUtils.isNotEmpty(adv.getMediaList())) {
            Media media = adv.getMediaList().get(0);
            String address = mediaService.getUniqueNameOnDisk(media, 1);
            return address;
        }

        return null;
    }

}
