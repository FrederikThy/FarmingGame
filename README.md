# FarmingGame

## Project Description

The purpose of this project is to create a farming game with a modular architecture.

The game is built around a shared ECS-style core:
- **Entities** represent objects in the world
- **Components** store data such as position, inventory, crops, and robot behavior
- **Systems** process entities each frame and implement game logic

This structure makes it easier to extend the game with new modules and features without changing major code.

## Features

- Modular game architecture
- JavaFX-based UI and rendering
- Robot entities with different roles:
  - Planting robot
  - Harvesting robot
  - Weed-removing robot
- Crop growth and planting systems
- Inventory and shop systems
- Pathfinding system with upgradeable algorithms
- Monitoring overlay for runtime metrics
- Map and tile-based world logic

## Project Structure

The repository is divided into multiple Maven modules:

- `Common-api`  
- `Common-ecs`  
- `Core-engine`  
- `RobotModule`  
- `CropsModule`  
- `InventoryModule`  
- `ShopModule`  
- `MapModule`  
- `PathfindingModule`  
- `MonitoringModule`  
- `ToolsModule`  
- `WeedModule`  

## Architecture

The game uses a modular plugin-based structure where modules contribute functionality through shared contracts.

Examples:
- `IGamePlugin` for spawning or initializing gameplay content
- `IEntityProcessingService` for update systems run each frame
- `IUiPluginService` for JavaFX UI overlays and plugin UI
- `IMapService` for rendering/map-related systems

The `Core-engine` loads these services and runs the game loop.

## Technologies Used

- Java
- JavaFX
- Maven
- Spring Context
- Java Module System

## How to Build

mvn clean install - in terminal/bash 

## How to Run
mvn exec:exec - in terminal/bash

## Typical gameplay
- The core engine starts the game
- Modules register plugins, systems, and UI elements
- The world is initialized
- Robots are spawned into the map
- Pathfinding assigns movement goals
- Robots move, plant, harvest, or remove weeds
- Inventory and shop systems update according to player actions
- Monitoring systems show runtime metrics
