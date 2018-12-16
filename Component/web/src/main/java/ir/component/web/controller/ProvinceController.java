package ir.component.web.controller;

import ir.component.core.dao.model.Province;
import ir.component.core.engine.ProvinceDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Controller
@Scope("view")
public class ProvinceController {

    private Province province = new Province();

    @Resource
    private ProvinceDao provinceDao;

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public void save() {
        provinceDao.persist(province);
    }

    public List<Province> getAllProvinces() {
        return provinceDao.allProvinces();
    }
}