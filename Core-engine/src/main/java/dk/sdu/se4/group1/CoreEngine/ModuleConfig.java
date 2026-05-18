package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.IGamePlugin;
import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import dk.sdu.se4.group1.CommonEcs.IUiPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

@Configuration(proxyBeanMethods = false)
public class ModuleConfig {

    @Bean
    public Game game(List<EcsSystem> ecsSystems,  List<IGamePlugin> plugins, List<IUiPlugin> uiPlugins) {
        return new Game(ecsSystems, plugins, uiPlugins);
    }

    @Bean
    public List<EcsSystem> ecsSystems() {
        List<EcsSystem> services = new ArrayList<>();
        ServiceLoader.load(EcsSystem.class).forEach(services::add);
        services.sort(Comparator.comparingInt(EcsSystem::priority));
        return services;
    }

    @Bean
    public List<IGamePlugin> gamePlugins() {
        List<IGamePlugin> plugins = new ArrayList<>();
        ServiceLoader.load(IGamePlugin.class).forEach(plugins::add);
        return plugins;
    }
    @Bean
    public List<IUiPlugin> uiPlugins() {
        List<IUiPlugin> plugins = new ArrayList<>();
        ServiceLoader.load(IUiPlugin.class).forEach(plugins::add);
        return plugins;
    }
}

