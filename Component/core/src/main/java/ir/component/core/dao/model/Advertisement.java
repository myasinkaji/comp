package ir.component.core.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Entity
public class Advertisement extends AbstractAdvertisement {


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisement")
//    @JoinColumn(name = "adv_id", nullable = false)
    private List<Media> mediaList;


    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

}
