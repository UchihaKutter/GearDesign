package geardesigner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author SUPERSTATION
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getResource("Main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("齿轮设计计算器");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        Image iconForLinux = new Image(getClass().getResourceAsStream(""));
        Image iconForWindows = new Image(getClass().getResourceAsStream(""));
        primaryStage.getIcons().addAll(iconForLinux, iconForWindows);
        primaryStage.show();
    }
}
