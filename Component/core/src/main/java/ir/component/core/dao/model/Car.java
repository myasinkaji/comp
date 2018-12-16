package ir.component.core.dao.model;

import javax.persistence.Entity;

/**
 * @author Mohammad Yasin Kaji
 */
//@Entity
public class Car extends BaseEntityInfo<Integer>{

    private String brand;
    private int year;
    private String color;
    private int price;
    private boolean soldState;

    public Car() {
    }

    public Car(String brand, int year, String color, int price, boolean soldState) {
        this.brand = brand;
        this.year = year;
        this.color = color;
        this.price = price;
        this.soldState = soldState;

    }

    public String getTitle() {
        return brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSoldState() {
        return soldState;
    }

    public void setSoldState(boolean soldState) {
        this.soldState = soldState;
    }
}
