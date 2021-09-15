package geardesigner.controls;

import geardesigner.Services;
import geardesigner.beans.Record;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RecordSelector {
    @FXML
    private TableView<Record> tvCalRecords;

    @FXML
    private TableColumn colNo;

    @FXML
    private TableColumn<Record, LocalDate> colDate;

    @FXML
    private TableColumn<Record, LocalTime> colTime;

    @FXML
    private TableColumn<Record, String> colParams;

    @FXML
    private DatePicker dpCalDate;

    @FXML
    private Button btCancel;

    @FXML
    private Button btAccept;

    private ObservableList<Record> items;
    private Record selected;

    public RecordSelector() {
        items = FXCollections.observableArrayList();
        selected = null;
    }

    @FXML
    void initialize() {
        initTable();
        initDatePicker();
        initButton();

        /**
         * 初始化的数据填充
         */
        final List<Record> initialRecords = Services.get().RecordBase().getRecentRecords();
        setItems(initialRecords);
    }

    private void initTable() {
        tvCalRecords.setItems(items);
        colNo.setCellFactory(new SerialNumCellFactory());
        colDate.setCellValueFactory(i -> new ReadOnlyObjectWrapper<>(i.getValue().getTimestamp().toLocalDate()));
        colTime.setCellValueFactory(i -> new ReadOnlyObjectWrapper<>(i.getValue().getTimestamp().toLocalTime()));
        colParams.setCellValueFactory(i -> new ReadOnlyStringWrapper(i.getValue().getSpecs().toString()));
    }

    private void initDatePicker() {
        dpCalDate.setOnHidden(e -> setDateFilter());
    }

    private void setDateFilter() {
        final LocalDate date = dpCalDate.getValue();
        if (date != null) {
            final List<Record> records = Services.get().RecordBase().getRecords(date);
            setItems(records);
        }
    }

    private void setItems(List<Record> records) {
        items.clear();
        items.addAll(records);
    }

    private void initButton() {
        btCancel.setOnAction(event -> btaCancel());
        btAccept.setOnAction(e -> btaAccept());
        /**
         * 未选中日期时，确定键不可用
         */
        tvCalRecords.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                btAccept.setDisable(true);
            } else {
                btAccept.setDisable(false);
            }
        });
    }

    private void btaCancel() {
        btCancel.getScene().getWindow().hide();
    }

    private void btaAccept() {
        selected = tvCalRecords.getSelectionModel().getSelectedItem();
        tvCalRecords.getScene().getWindow().hide();
    }

    /**
     * 获取选中项
     *
     * @return 一条历史记录，或null表示未指定记录
     */
    @Nullable
    public Record result() {
        return selected;
    }
}
