package dk.sdu.se4.group1.CommonEcs;

// Interface to implement when wanting to instantiate entities in modules.
public interface IGamePlugin {
    void start(World world);

    void stop(World world);
}
