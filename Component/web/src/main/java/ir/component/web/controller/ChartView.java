package ir.component.web.controller;


import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Scope("view")
public class ChartView implements Serializable {


    private DonutChartModel donutModel1;
    private DonutChartModel donutModel2;


    @PostConstruct
    public void init() {

        createDonutModels();

    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    public DonutChartModel getDonutModel1() {
        return donutModel1;
    }

    public DonutChartModel getDonutModel2() {
        return donutModel2;
    }


    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();

        ChartSeries processs = new ChartSeries();
        processs.setLabel("Boys");
        processs.set("2004", 120);
        processs.set("2005", 100);
        processs.set("2006", 44);
        processs.set("2007", 150);
        processs.set("2008", 25);

        ChartSeries tasks = new ChartSeries();
        tasks.setLabel("Girls");
        tasks.set("2004", 52);
        tasks.set("2005", 60);
        tasks.set("2006", 110);
        tasks.set("2007", 90);
        tasks.set("2008", 120);

        model.addSeries(processs);
        model.addSeries(tasks);

        return model;
    }


    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Process");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Tasks");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);

        model.addSeries(boys);
        model.addSeries(girls);

        return model;
    }


    private BubbleChartModel initBubbleModel() {
        BubbleChartModel model = new BubbleChartModel();

        model.add(new BubbleChartSeries("Majles", 70, 183, 55));
        model.add(new BubbleChartSeries("Naft", 45, 92, 36));
        model.add(new BubbleChartSeries("Tejarat", 24, 104, 40));
        model.add(new BubbleChartSeries("Magfa", 50, 123, 60));
        model.add(new BubbleChartSeries("ITO", 15, 89, 25));
        model.add(new BubbleChartSeries("Afs", 40, 180, 80));
        model.add(new BubbleChartSeries("Kaji", 70, 70, 48));

        return model;
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");

        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }


    private void createDonutModels() {
        donutModel1 = initDonutModel();
        donutModel1.setTitle("Process Chart");
        donutModel1.setLegendPosition("w");

        donutModel2 = initDonutModel();
        donutModel2.setTitle("Task Chart");
        donutModel2.setLegendPosition("e");
        donutModel2.setSliceMargin(5);
        donutModel2.setShowDataLabels(true);
        donutModel2.setDataFormat("value");
        donutModel2.setShadow(false);
    }

    private DonutChartModel initDonutModel() {
        DonutChartModel model = new DonutChartModel();

        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
        circle1.put("Open Task", 150);
        circle1.put("Suspend Task", 400);
        circle1.put("Closed Task", 200);
        circle1.put("Claim Task", 10);
        model.addCircle(circle1);

        Map<String, Number> circle2 = new LinkedHashMap<String, Number>();
        circle2.put("Open Task", 540);
        circle2.put("Suspend Task", 125);
        circle2.put("Closed Task", 702);
        circle2.put("Claim Task", 421);
        model.addCircle(circle2);

        Map<String, Number> circle3 = new LinkedHashMap<String, Number>();
        circle3.put("Open Task", 40);
        circle3.put("Suspend Task", 325);
        circle3.put("Closed Task", 402);
        circle3.put("Claim Task", 421);
        model.addCircle(circle3);

        return model;
    }
}