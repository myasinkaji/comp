package ir.component.web.service;

import ir.component.core.dao.model.BaseEntityInfo;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * @author Zahra Afsharinia
 */
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter, Serializable {

    private static final String ID_CLASSNAME_SEPARATOR = "$";

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            String[] classNameId = value.split("\\" + ID_CLASSNAME_SEPARATOR + "\\" + ID_CLASSNAME_SEPARATOR);
            Object id = classNameId[1];
            Class entityClass = Class.forName(classNameId[0]);
            Constructor constructor = entityClass.getConstructor();
            BaseEntityInfo entity = (BaseEntityInfo) constructor.newInstance();
            entity.setId(id);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

       /* Guild guild = new Guild();
        String id  = value.split("\\$")[2];
        guild.setId(Integer.valueOf(id));
        return guild;*/

/*
        Province province = new Province();
        String id = value.split("\\$")[2];
        province.setId(Integer.valueOf(id));
        return province;
*/

    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        BaseEntityInfo obj = (BaseEntityInfo) value;
        String stringValue = obj.getClass().getName() + ID_CLASSNAME_SEPARATOR + ID_CLASSNAME_SEPARATOR + obj.getId();

        return stringValue;
    }
}
