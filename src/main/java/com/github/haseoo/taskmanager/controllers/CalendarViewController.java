package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class CalendarViewController {

    private final AtomicReference<TaskListView> taskListView;

    @FXML
    private DatePicker dateInput;
    @FXML
    private ComboBox<String> type;

    @FXML
    void initialize() {
        dateInput.valueProperty().addListener(this::onDateUpdate);
        type.valueProperty().addListener(this::onTypeUpdate);
    }


    private void onDateUpdate(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
        updateTable();

    }
    private void onTypeUpdate(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (dateInput.getValue() != null) {
            updateTable();
        }

    }

    private void updateTable() {
        System.out.println(dateInput.getValue());
        System.out.println(type.getSelectionModel().getSelectedIndex());
    }
}
