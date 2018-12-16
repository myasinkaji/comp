package ir.component.core.dao.model;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */

@MappedSuperclass
public abstract class AbstractAdvertisement extends BaseEntityInfo<Integer> {

    private String title;
    @ManyToOne
    private Guild guild;
    private Date fromValidityDate;
    private Date toValidityDate;
    private Double price;
    private Double purchaseDiscount;
    private Double saleCommission;
    private String description;


    public AbstractAdvertisement() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Date getFromValidityDate() {
        return fromValidityDate;
    }

    public void setFromValidityDate(Date fromValidityDate) {
        this.fromValidityDate = fromValidityDate;
    }

    public Date getToValidityDate() {
        return toValidityDate;
    }

    public void setToValidityDate(Date toValidityDate) {
        this.toValidityDate = toValidityDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPurchaseDiscount() {
        return purchaseDiscount;
    }

    public void setPurchaseDiscount(Double purchaseDiscount) {
        this.purchaseDiscount = purchaseDiscount;
    }

    public Double getSaleCommission() {
        return saleCommission;
    }

    public void setSaleCommission(Double saleCommission) {
        this.saleCommission = saleCommission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractAdvertisement that = (AbstractAdvertisement) o;

        if (getId() == that.getId())
            return true;

        else
            return false;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
//        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (fromValidityDate != null ? fromValidityDate.hashCode() : 0);
        result = 31 * result + (toValidityDate != null ? toValidityDate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (purchaseDiscount != null ? purchaseDiscount.hashCode() : 0);
        result = 31 * result + (saleCommission != null ? saleCommission.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
//        result = 31 * result + (files != null ? files.hashCode() : 0);
        return result;
    }
}
