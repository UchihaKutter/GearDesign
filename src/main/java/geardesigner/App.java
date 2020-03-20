package geardesigner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root=fxmlLoader.load();
        primaryStage.setTitle("齿轮设计计算器");
        primaryStage.setScene(new Scene(root));
        Image iconForLinux=new Image(getClass().getResourceAsStream(""));
        Image iconForWindows=new Image(getClass().getResourceAsStream(""));
        primaryStage.getIcons().addAll(iconForLinux,iconForWindows);
        primaryStage.show();
    }
}
