package ir.component.web.config;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * Implements the JSF2 View Scope for use by Spring. This class is registered as a Spring bean with the CustomScopeConfigurer.
 * <p/>
 * Taken from: http://www.harezmi.com.tr/spring-view-scope-for-jsf-2-users/
 * 
 * @author Mohammad Yasin Kaji
 */
public class SpringViewScope implements Scope {
    public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callbacks";

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object get(String name, ObjectFactory objectFactory) {
        Map viewMap = getViewMap();
        Object instance = viewMap.get(name);
        if (instance == null) {
            instance = objectFactory.getObject();
            synchronized (viewMap) {
                viewMap.put(name, instance);
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public Object remove(String name) {
        Object instance = getViewMap().remove(name);
        if (instance != null) {
            Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
            if (callbacks != null) {
                callbacks.remove(name);
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void registerDestructionCallback(String name, Runnable runnable) {
        Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
        if (callbacks != null) {
            callbacks.put(name, runnable);
        }
    }

    public Object resolveContextualObject(String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
        return facesRequestAttributes.resolveReference(name);
    }

    public String getConversationId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
        return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
    }

    private Map<String, Object> getViewMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new FacesException("FacesContext is not yeat constructed");
        }
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot == null) {
            throw new FacesException("FacesContext.viewRoot is null");
        }
        return viewRoot.getViewMap();
    }
}