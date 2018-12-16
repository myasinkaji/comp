package ir.component.web.service;

import ir.component.core.dao.model.UserType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;

/**
 * @author Zahra Afsharinia
 */
@FacesConverter(value = "enumConverter")
public class EnumConverter implements Converter, Serializable {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return UserType.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
