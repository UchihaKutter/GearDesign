package geardesigner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.net.URL;
import java.util.List;

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
        primaryStage.getIcons().addAll(loadIcons());
        primaryStage.show();
    }

    private @NotNull @Unmodifiable List<Image> loadIcons() {
        final Class<?> clz = getClass();
        final Image size32 = new Image(clz.getResourceAsStream("icon/32x32.png"));
        final Image size64 = new Image(clz.getResourceAsStream("icon/64x64.png"));
        final Image size128 = new Image(clz.getResourceAsStream("icon/128x128.png"));
        final Image size256 = new Image(clz.getResourceAsStream("icon/256x256.png"));
        final Image size512 = new Image(clz.getResourceAsStream("icon/512x512.png"));
        final Image size768 = new Image(clz.getResourceAsStream("icon/768x768.png"));
        return List.of(size768, size512, size256, size128, size64, size32);
    }
}
