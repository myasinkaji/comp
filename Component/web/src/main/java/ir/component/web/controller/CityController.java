package ir.component.web.controller;

import ir.component.core.dao.model.City;
import ir.component.core.engine.CityDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Controller
@Scope("view")
public class CityController {

    private City city = new City();

    @Resource
    private CityDao cityDao;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void save() {
        cityDao.persist(city);
    }

    public List<City> getAllCities() {
        return cityDao.allCities();
    }
}
