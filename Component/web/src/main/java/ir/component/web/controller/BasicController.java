package ir.component.web.controller;

import ir.component.core.dao.model.Car;
import ir.component.core.engine.CarDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Controller("carController")
@Scope("view")
public class BasicController implements Serializable {

    private Car car = new Car();

    @Resource
    private CarDao carDao;
 
    @PostConstruct
    public void init() {
        car.setBrand("Kaji");
    }

    public void save() {
        carDao.persist(car);
    }
    public List<Car> getCars() {
        return carDao.allCars();
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}