package dk.sdu.se4.group1.Map;

import dk.sdu.se4.group1.CommonEcs.EntityID;
import dk.sdu.se4.group1.CommonEcs.GoalComponent;
import dk.sdu.se4.group1.CommonEcs.PositionComponent;
import dk.sdu.se4.group1.CommonEcs.RenderComponent;
import dk.sdu.se4.group1.CommonEcs.World;

/*
Creates the goal-marker entity — the visible destination tile (point B).
The MappingSystem will render it in gold because of its RenderComponent colour.  */
public class GoalFactory {

    public static EntityID createGoal(World world, int x, int y) {
        EntityID goal = world.createEntity();
        world.addComponent(goal, new PositionComponent(x, y));
        world.addComponent(goal, new RenderComponent("GOLD"));
        world.addComponent(goal, new GoalComponent());
        return goal;
    }
}
