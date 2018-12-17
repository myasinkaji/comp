package ir.component.core.dao.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Data model of Permission that is registered by student
 *
 * @author Mohammad Yasin Kaji
 *
 */
//@Entity
//@Table
public class KajiArrivalAndExit extends  BaseEntityInfo<Long>{
    private String k_date;
    private String k_day;
    private String k_arrival;
    private String k_sum;
    private String k_exit;

    public KajiArrivalAndExit() {
    }


    @Override
    public String getTitle() {
        return null;
    }

    public String getK_date() {
        return k_date;
    }

    public void setK_date(String k_date) {
        this.k_date = k_date;
    }

    public String getK_day() {
        return k_day;
    }

    public void setK_day(String k_day) {
        this.k_day = k_day;
    }

    public String getK_arrival() {
        return k_arrival;
    }

    public void setK_arrival(String k_arrival) {
        this.k_arrival = k_arrival;
    }

    public String getK_sum() {
        return k_sum;
    }

    public void setK_sum(String k_sum) {
        this.k_sum = k_sum;
    }

    public String getK_exit() {
        return k_exit;
    }

    public void setK_exit(String k_exit) {
        this.k_exit = k_exit;
    }
}
