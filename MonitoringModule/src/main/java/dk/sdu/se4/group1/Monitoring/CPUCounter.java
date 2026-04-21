package dk.sdu.se4.group1.Monitoring;

import com.sun.management.OperatingSystemMXBean;
import dk.sdu.se4.group1.CommonEcs.IMoniteringService;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.lang.management.ManagementFactory;

public class CPUCounter extends Label implements IMoniteringService {

    //Gives us access to the OS metrics
    //We use com.sun.management.OperatingSystemMXBean so we can use the getProcessCpuLoad()
    private final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    //The Bean we use (osBean) gives us access to data


    private double cpuPercent = 0;
    private double timeAccumulator = 0;

    //The code under is the same as in FPScounter
    public CPUCounter() {
        super("CPU: 0%");
        setTextFill(Color.ORANGE);
        setFont(Font.font("Verdana", FontWeight.LIGHT, 10));
        setLayoutX(10);
        setLayoutY(26);
        setMouseTransparent(true);
    }

    @Override
    public String getName() {
        return "CPU";
    }

    @Override
    public void reset() {
        cpuPercent = 0.0;
        timeAccumulator = 0.0;
        setText("CPU: 0%");
    }

    @Override
    public void OnFrame(double dt) {
        timeAccumulator += dt;
        if(timeAccumulator >= 1.0) {
            double load = osBean.getProcessCpuLoad(); //If the cpu load is 0.0, then its idle
            //and its loading it into 1.0 - using 100% of cpu
            //and getProcessCpuload tells how much cpu we use
            if (load > 0.0) {
                cpuPercent = load * 100.0; //Converts it into to percentage
                setText(String.format("CPU: %.1f%%", cpuPercent));
            }
            timeAccumulator = 0.0;
        }
    }

    @Override
    public double getValue() {
        return cpuPercent;
    }

    @Override
    public String getUnit() {
        return "%";
    }
}
