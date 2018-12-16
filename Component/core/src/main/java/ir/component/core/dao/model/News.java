package ir.component.core.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Zahra Afsharinia
 */
@Entity
public class News extends BaseEntityInfo<Integer> {

    @Column(length = 1000)
    private String text;
    @Column(length = 1000)
    private String subject;

    public News() {
    }

    public News(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return subject;
    }
}
