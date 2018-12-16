package ir.component.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Controller
@Scope("view")
public class DataExporterView implements Serializable {
     
    private List<Car> cars;
         
    @Resource
    private CarService service;
     
    @PostConstruct
    public void init() {
        cars = service.createCars(100);
    }
 
    public List<Car> getCars() {
        return cars;
    }
 
    public void setService(CarService service) {
        this.service = service;
    }
}