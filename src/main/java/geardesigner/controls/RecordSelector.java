package geardesigner.controls;

import geardesigner.Log;
import geardesigner.Services;
import geardesigner.beans.Record;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 历史记录选择器的抽象
 *
 * @author SUPERTOP
 */
public class RecordSelector {
    private final Parent root;
    private final Scene scene;
    private final Stage stage;
    private final RecordSelectorController controller;

    public RecordSelector(Window owner) throws IOException {
        URL resource = getClass().getClassLoader().getResource("geardesigner/controls/RecordSelector.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(resource);
        root = fxmlLoader.load();
        scene = new Scene(root);
        stage = initStage(owner, scene);
        controller = fxmlLoader.getController();
        Log.info("创建了新的历史记录选择器实例");
    }

    static Stage initStage(@NotNull Window owner, @NotNull Scene scene) {
        final Stage newStage = new Stage();
        newStage.setTitle("选择一个历史记录...");
        newStage.setScene(scene);
        newStage.sizeToScene();
        newStage.setResizable(false);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(owner);
        return newStage;
    }

    /**
     * 初始化数据填充，并显示历史记录选择器。选择结束后，返回一条记录或null
     *
     * @return 一条{@code Record}或null
     */
    public @Nullable Record showAndWait() {
        final List<Record> initialRecords = Services.get().RecordBase().getRecentRecords();
        controller.reset();
        controller.setItems(initialRecords);
        stage.showAndWait();
        return controller.result();
    }
}
