package geardesigner.controls;

import geardesigner.Services;
import geardesigner.beans.Record;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

/**
 * 历史记录选择对话框的控制器
 *
 * @author SUPERTOP
 */
public class RecordSelectorController {
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

    /**
     * 日期过滤器的调用
     */
    private ObservableSet<LocalDate> AccentDates;

    public RecordSelectorController() {
        items = FXCollections.observableArrayList();
        selected = null;
        AccentDates = FXCollections.observableSet(new HashSet<>());
    }

    @FXML
    void initialize() {
        initTable();
        initDatePicker();
        initButton();
    }

    private void initTable() {
        tvCalRecords.setItems(items);
        colNo.setCellFactory(new SerialNumCellFactory());
        colDate.setCellValueFactory(i -> new ReadOnlyObjectWrapper<>(i.getValue().getTimestamp().toLocalDate()));
        colTime.setCellValueFactory(i -> new ReadOnlyObjectWrapper<>(i.getValue().getTimestamp().toLocalTime()));
        colParams.setCellValueFactory(i -> new ReadOnlyStringWrapper(i.getValue().getSpecs().toString()));
        tvCalRecords.setPlaceholder(new Label("无历史记录"));
    }

    private void initDatePicker() {
        dpCalDate.setDayCellFactory(dp -> new AccentDateCell(AccentDates));
        dpCalDate.setOnHidden(e -> setDateFilter());
        /**
         * 设置初始的日期高亮
         */
        final List<LocalDate> recordDates = Services.get().RecordBase().getRecordDates();
        AccentDates.addAll(recordDates);
    }

    void reset() {
        selected = null;
        dpCalDate.setValue(null);
        /**
         * 刷新日期高亮
         */
        AccentDates.clear();
        final List<LocalDate> recordDates = Services.get().RecordBase().getRecordDates();
        AccentDates.addAll(recordDates);
    }

    void setItems(List<Record> records) {
        items.clear();
        items.addAll(records);

    }

    private void setDateFilter() {
        final LocalDate date = dpCalDate.getValue();
        if (date != null) {
            final List<Record> records = Services.get().RecordBase().getRecords(date);
            setItems(records);
        }
    }

    private void initButton() {
        btCancel.setOnAction(event -> btaCancel());
        btAccept.setOnAction(e -> btaAccept());
        /**
         * 未选中日期时，确定键不可用
         */
        tvCalRecords.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                btAccept.setDisable(newValue == null));
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
    Record result() {
        return selected;
    }
}
