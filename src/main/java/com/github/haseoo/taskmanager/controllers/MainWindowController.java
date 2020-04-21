package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;
import static com.github.haseoo.taskmanager.utilities.Utilities.prepareDialog;

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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conform operation");
        alert.setHeaderText("Are you sure to create new list?");
        alert.setContentText("All unsaved work will be lost");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //TODO
            }
        });
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
    void onTag() {

    }

    @FXML
    void onInfo() {

    }

    @FXML
    void onAddSlot() {

    }

    @FXML
    void onTitleEdit() {
        TextInputDialog dialog = new TextInputDialog(taskListTitle.getText());
        dialog.setTitle("Edit task list name");
        dialog.setHeaderText("Enter new name");
        dialog.showAndWait().ifPresent(name -> taskListView.getTitle().setValue(name));
    }
}
