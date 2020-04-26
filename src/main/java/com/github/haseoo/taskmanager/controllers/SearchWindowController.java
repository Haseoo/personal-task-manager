package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TagView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskListView;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SearchWindowController {
    private final AtomicReference<TaskListView> taskListView;

    @FXML
    private ComboBox<String> tagInput;

    @FXML
    private ComboBox<String> statusInput;

    @FXML
    private DatePicker fromDateInput;

    @FXML
    private DatePicker toDateInput;

    @FXML
    private TextField taskNameInput;

    @FXML
    private TableView<?> resultTable;

    @FXML
    void initialize() {
        tagInput.getItems().addAll(taskListView.get().getTags()
                .stream()
                .map(TagView::getName)
                .map(SimpleStringProperty::getValueSafe)
                .collect(toList()));
    }

    @FXML
    void onSearch() {

    }

    @FXML
    void onReset() {
        tagInput.setValue(tagInput.getItems().get(0));
        statusInput.setValue(statusInput.getItems().get(0));
        fromDateInput.setValue(null);
        toDateInput.setValue(null);
        taskNameInput.setText(null);
    }
}
