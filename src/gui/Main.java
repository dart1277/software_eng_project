package gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * The main application class
 * */

public class Main extends Application {

    /**
     * Initializes the stage for javafx
     *
     * @param primaryStage primary stage for the app
     * @throws Exception on failure throw exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        double width = 1024;
        double height = 600;
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.setTitle("Encryptor");

        primaryStage.show();
    }

    private static void correctEncoding() {
        try {
            System.setProperty("file.encoding", "UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the application
     * @param args application args to pass to the launch method
     * */
    public static void main(String[] args) {
        correctEncoding();
        launch(args);
    }
}
