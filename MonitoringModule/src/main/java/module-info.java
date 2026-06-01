import dk.sdu.se4.group1.CommonEcs.IUiPluginService;

module MonitoringModule {
    requires javafx.controls;
    requires Common.ecs;
    requires jdk.management;

    exports dk.sdu.se4.group1.Monitoring;

    provides IUiPluginService with dk.sdu.se4.group1.Monitoring.MonitoringUiPluginService;
}