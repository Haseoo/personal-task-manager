package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.OpenWindowTableRecord;
import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.deleteConfirmationDialog;

@RequiredArgsConstructor
public class OpenTaskWindowController {
    private final AtomicReference<TaskListView> taskListView;

    @FXML
    TableView<OpenWindowTableRecord> tasksLists;

    @FXML
    void initialize() {
        tasksLists.getItems().add(new OpenWindowTableRecord("Test1", LocalDateTime.now()));
        tasksLists.getItems().add(new OpenWindowTableRecord("Test2", LocalDateTime.of(2019, 12, 30, 12, 11)));
    }

    @FXML
    void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) tasksLists.getScene().getWindow()).close();
    }

    @FXML
    void onOpen() {
        OpenWindowTableRecord selected = tasksLists.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskListView.get().getTitle().setValue(selected.getTasksListName());
            closeWindow();
        }
    }

    @FXML
    void onImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Task list file", "*.tsk"));
        File file = fileChooser.showOpenDialog((Stage) tasksLists.getScene().getWindow());
        if (file != null) {
            tasksLists.getItems().add(new OpenWindowTableRecord(file.getName(), LocalDateTime.now()));
        }
    }

    @FXML
    void onRemove() {
        var selected = tasksLists.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteConfirmationDialog(String.format("Are you sure to delete %s?", selected.getTasksListName()),
                    () -> tasksLists.getItems().remove(selected));
        }
    }
}