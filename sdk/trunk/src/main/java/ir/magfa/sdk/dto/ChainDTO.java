package ir.magfa.sdk.dto;

import java.io.Serializable;

/**
 * @author Mohammad Yasin Kaji
 */
public class ChainDTO implements Serializable {

    private static final long UUID = 1L;

    private int id;
    private String productName;
    private int productCode;
    private double price;

    public ChainDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
