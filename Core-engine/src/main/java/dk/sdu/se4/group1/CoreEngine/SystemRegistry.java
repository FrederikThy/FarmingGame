package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.World;

import java.util.ArrayList;
import java.util.List;

public class SystemRegistry {
    private final List<EcsSystem> systems = new ArrayList<>();

    public void register (EcsSystem system){
        systems.add(system);
    }

    public void updateAll(World world, double DeltaTime){
        for(EcsSystem system : systems){
            system.update(world, DeltaTime);
        }
    }
}
