package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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
    void initialize() {
        dateInput.valueProperty().addListener(this::onDateUpdate);
    }


    private void onDateUpdate(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
        System.out.println(newValue);
    }
}
