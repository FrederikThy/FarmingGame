package dk.sdu.se4.group1.CoreEngine;

import dk.sdu.se4.group1.CommonEcs.EcsSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

@Configuration(proxyBeanMethods = false)
public class ModuleConfig {

    @Bean
    public Game game(List<EcsSystem> ecsSystems) {
        return new Game(ecsSystems);
    }

    @Bean
    public List<EcsSystem> ecsSystems() {
        List<EcsSystem> services = new ArrayList<>();
        ServiceLoader.load(EcsSystem.class).forEach(services::add);
        services.sort(Comparator.comparingInt(EcsSystem::priority));
        return services;
    }
}

