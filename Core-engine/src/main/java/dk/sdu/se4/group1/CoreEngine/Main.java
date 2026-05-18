package dk.sdu.se4.group1.CoreEngine;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    private AnnotationConfigApplicationContext ctx;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {
        ctx = new AnnotationConfigApplicationContext(ModuleConfig.class);

        Game game = ctx.getBean(Game.class);
        game.start(window);
    }

    @Override
    public void stop() {
        if (ctx != null) {
            ctx.close();
        }
    }
}
