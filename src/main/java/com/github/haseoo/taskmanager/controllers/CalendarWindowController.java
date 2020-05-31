package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.TableViewCallback;
import com.github.haseoo.taskmanager.views.TaskView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class CalendarWindowController {
    private final JFXServiceImpl jfxService;

    @FXML
    private DatePicker dateInput;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TableView<TaskView> resultTable;
    @FXML
    private Button refreshBtn;

    @FXML
    void initialize() {
        dateInput.valueProperty().addListener(this::onDateUpdate);
        type.valueProperty().addListener(this::onTypeUpdate);
        resultTable.setRowFactory(new TableViewCallback(jfxService));
    }

    @FXML
    void onRefresh() {
        updateTable();
    }


    private void onDateUpdate(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
        refreshBtn.setDisable(newValue == null);
        type.setDisable(newValue == null);
        updateTable();

    }

    private void onTypeUpdate(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        updateTable();

    }

    private void updateTable() {
        var tasks = jfxService.getTasksByCondition(getFilterCondition()).stream().map(TaskView::from).collect(toList());
        resultTable.getItems().clear();
        resultTable.getItems().addAll(tasks);
    }

    private Predicate<TaskData> getFilterCondition() {
        var selectedDate = dateInput.getValue();
        switch (type.getSelectionModel().getSelectedIndex()) {
            case 0:
                return taskData -> (taskData.getDateFrom() != null && taskData.getDateFrom().equals(selectedDate)) ||
                        (taskData.getDateTo() != null && taskData.getDateTo().equals(selectedDate));
            case 1:
                return taskData -> taskData.getDateFrom() != null && taskData.getDateFrom().equals(selectedDate);
            case 2:
                return taskData -> taskData.getDateTo() != null && taskData.getDateTo().equals(selectedDate);
            default:
                throw new AssertionError();
        }
    }
}
