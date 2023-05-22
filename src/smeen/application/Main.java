package smeen.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import smeen.global.SmeenFileChooser;
import smeen.views.MainView;

import java.util.concurrent.CompletableFuture;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SmeenFileChooser.initialize(stage);

        MainView mainView = new MainView(stage);
        Scene mainScene = new Scene(mainView);
        mainView.registerEvents(mainScene);

        stage.setTitle("Sniff");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/picture/dog-icon.png")));
        stage.setScene(mainScene);
        stage.show();
    }
}