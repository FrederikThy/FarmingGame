package dk.sdu.se4.group1.Monitoring;

import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.layout.Pane;

public class MonitoringOverlay extends Pane implements IEntityProcessingService {
    private final FPSCounter fpsCounter;
    private final CPUCounter cpuCounter;
    private final MemoryCounter memoryCounter;
    private final PathfindingHUDLabel pathfindingHUDLabel;

    public MonitoringOverlay(World world) {
        this.fpsCounter = new FPSCounter();
        this.cpuCounter = new CPUCounter();
        this.memoryCounter = new MemoryCounter();
        this.pathfindingHUDLabel = new PathfindingHUDLabel(world);

        setMouseTransparent(true);
        setPickOnBounds(false);
        getChildren().addAll(fpsCounter, cpuCounter, memoryCounter, pathfindingHUDLabel);
    }

    @Override
    public void update(World world, double deltaTime) {
        fpsCounter.OnFrame(deltaTime);
        cpuCounter.OnFrame(deltaTime);
        memoryCounter.OnFrame(deltaTime);
        pathfindingHUDLabel.onFrame();
    }
}

