package ir.component.core.dao.model;

import javax.persistence.*;
import java.io.File;

/**
 * @author Mohammad Yasin Kaji
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "media_key")
})
public class Media extends BaseEntityInfo<Long> {

    @Column(length = 10)
    private String filename;
    @Column(length = 10)
    private String originalFilename;

    @Column(name = "original_size", nullable = true)
    private Integer originalSize = 0;

    @Column(name = "original_format",
            nullable = false,
            length = 5)
    private String originalFormat;

    @Column(name = "media_key", length = 10)
    private String key;

    @Transient
    private File file;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adv_id", nullable = false)
    private Advertisement advertisement;

    public Media() {
    }

    @Override
    public String getTitle() {
        return filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Integer getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(Integer originalSize) {
        this.originalSize = originalSize;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
