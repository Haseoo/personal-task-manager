package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;

public class MainWindowController {
    private final Application application;

    private TaskListView taskListView;

    @FXML
    private Label taskListTitle;

    public MainWindowController(Application application, TaskListView taskListView) {
        this.application = application;
        this.taskListView = taskListView;
    }

    @FXML
    void initialize() {
        taskListTitle.textProperty().bindBidirectional(taskListView.getTitle());
    }

    @FXML
    void onNew() {
        confirmationDialog("Are you sure to create new list?", "All unsaved work will be lost", () -> {
        });
        //TODO Add action
    }

    @FXML
    void onOpen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/openTaskList.fxml"));
        loader.setController(new OpenTaskWindowController(new AtomicReference<>(taskListView)));
        Parent root = loader.load();
        prepareDialog(root, "Open task list");
    }

    @FXML
    void onSave() {

    }

    @FXML
    void onExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export task list");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Task list file", "*.tsk"));
        fileChooser.showSaveDialog((Stage) taskListTitle.getScene().getWindow());
    }

    @FXML
    void onCalendar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/calendar.fxml"));
        loader.setController(new CalendarViewController(new AtomicReference<>(taskListView)));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Calendar view");
        stage.show();
    }

    @FXML
    void onSearch() {

    }

    @FXML
    void onTag() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/tagWindow.fxml"));
        loader.setController(new TagWindowController(new AtomicReference<>(taskListView)));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Tag view");
        stage.show();

    }

    @FXML
    void onInfo() {

    }

    @FXML
    void onAddSlot() {

    }

    @FXML
    void onTitleEdit() {
        textInputDialog(taskListTitle.getText(),
                "Edit task list name",
                "Enter new name",
                name -> taskListView.getTitle().setValue(name));
    }
}
