package dk.sdu.se4.group1.Monitoring;

import dk.sdu.se4.group1.CommonEcs.IMoniteringService;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class MemoryCounter extends Label implements IMoniteringService{

    //Again we use a java Bean which gives us the heap memory
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    private double memoryUsed = 0;
    private double timeAccumulator = 0;

    public MemoryCounter() {
        super("Memory: 0 MB");
        setTextFill(Color.CYAN);
        setFont(Font.font("Verdana", FontWeight.LIGHT, 10));
        setLayoutX(10);
        setLayoutY(42);
        setMouseTransparent(true);
    }

    @Override
    public String getName() {
        return "Memory";
    }

    @Override
    public void reset() {
        memoryUsed = 0;
        timeAccumulator = 0;
        setText("MEM: 0 MB");
    }

    @Override
    public void OnFrame(double dt) {
        timeAccumulator += dt;
        if(timeAccumulator >= 1.0) {
            long usedBytes = memoryMXBean.getHeapMemoryUsage().getUsed(); //Gives us bytes used in the game
            memoryUsed = usedBytes / (1024.0 * 1024.0); //Converts the bytes into MB
            setText(String.format("Memory: %.1f MB", memoryUsed));
            timeAccumulator = 0;
        }
    }

    @Override
    public double getValue() {
        return memoryUsed;
    }

    @Override
    public String getUnit() {
        return "MB";
    }
}
