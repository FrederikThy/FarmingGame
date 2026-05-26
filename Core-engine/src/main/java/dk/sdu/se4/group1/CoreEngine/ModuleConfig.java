package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.IEntityProcessingService;
import dk.sdu.se4.group1.CommonEcs.IMapService;
import dk.sdu.se4.group1.CommonEcs.IUiPluginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

@Configuration(proxyBeanMethods = false)
public class ModuleConfig {

    @Bean
    public Game game(List<IEntityProcessingService> IEntityProcessingServices, List<IGamePlugin> plugins, List<IUiPluginService> uiPlugins, List<IMapService> renderSystems) {
        return new Game(IEntityProcessingServices, plugins, uiPlugins, renderSystems);
    }

    // Regular systems
    @Bean
    public List<IEntityProcessingService> ecsSystems() {
        List<IEntityProcessingService> services = new ArrayList<>();
        ServiceLoader.load(IEntityProcessingService.class).forEach(services::add);
        services.sort(Comparator.comparingInt(IEntityProcessingService::priority));
        return services;
    }

    // Regular plugins
    @Bean
    public List<IGamePlugin> gamePlugins() {
        List<IGamePlugin> plugins = new ArrayList<>();
        ServiceLoader.load(IGamePlugin.class).forEach(plugins::add);
        return plugins;
    }

    // For modules with plugins that contains javafx elements
    @Bean
    public List<IUiPluginService> uiPlugins() {
        List<IUiPluginService> plugins = new ArrayList<>();
        ServiceLoader.load(IUiPluginService.class).forEach(plugins::add);
        return plugins;
    }

    // Since our mappingSystem uses GraphicalContext, we need to have a serviceloader for that single system
    @Bean
    public List<IMapService> renderSystems() {
        List<IMapService> systems = new ArrayList<>();
        ServiceLoader.load(IMapService.class).forEach(systems::add);
        return systems;
    }
}

