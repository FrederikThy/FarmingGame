package dk.sdu.se4.group1.Monitoring;

import dk.sdu.se4.group1.CommonEcs.Components.PathfindingUpgradeIComponentService;
import dk.sdu.se4.group1.CommonEcs.World;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * HUD label showing the active pathfinding algorithm.
 * Sits below FPS (Y=10), CPU (Y=26), Memory (Y=42) at Y=58.
 * Colour changes with tier: BFS = white, Dijkstra = yellow, A* = cyan.
 */
public class PathfindingHUDLabel extends Label {

    private final World world;
    private PathfindingUpgradeIComponentService.AlgorithmTier lastTier = null;

    public PathfindingHUDLabel(World world) {
        super("Pathfinding: BFS");
        this.world = world;
        setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        setTextFill(Color.WHITE);
        setLayoutX(10);
        setLayoutY(58);
        setMouseTransparent(true);
    }

    public void onFrame() {
        var upgradeEntities = world.getEntitiesWith(PathfindingUpgradeIComponentService.class);
        if (upgradeEntities == null || !upgradeEntities.iterator().hasNext()) return;

        PathfindingUpgradeIComponentService upgrade = (PathfindingUpgradeIComponentService)
                world.GetComponent(upgradeEntities.iterator().next(), PathfindingUpgradeIComponentService.class);

        if (upgrade.activeTier == lastTier) return;
        lastTier = upgrade.activeTier;

        setText("Pathfinding: " + upgrade.activeTier.displayName);
        switch (upgrade.activeTier) {
            case BFS      -> setTextFill(Color.WHITE);
            case DIJKSTRA -> setTextFill(Color.YELLOW);
            case A_STAR   -> setTextFill(Color.CYAN);
        }
    }
}
