package dk.sdu.se4.group1.CommonEcs;

import javafx.scene.canvas.GraphicsContext;

public interface IRenderSystem {
    EcsSystem create(GraphicsContext gc);
}
