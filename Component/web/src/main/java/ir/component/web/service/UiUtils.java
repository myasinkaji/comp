package ir.component.web.service;

import org.springframework.stereotype.Service;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Service
public class UiUtils {

    public static void refresh() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public static void redirect(String address) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(address);
    }



    public List<SelectItem> asSelectItem(String bundleKey, int from, int count) {
        return asFilterItem(null, bundleKey, from, count);
    }

    public List<SelectItem> asFilterItem(String firstItem, String bundleKey, int from, int count) {
        List<SelectItem> l = new ArrayList<SelectItem>();
        if (firstItem != null) {
            l.add(new SelectItem("", firstItem));
        }
        for (int i = from; i < from + count; i++) {
            l.add(new SelectItem(i, "Admin" + i));
        }
        return l;
    }
}
