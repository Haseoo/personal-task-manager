package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskListView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;

public class MainWindowController {
    private final Application application;

    private TaskListView taskListView;
    private List<SlotController> slots;

    @FXML
    private Label taskListTitle;
    @FXML
    private HBox mainHBox;

    public MainWindowController(Application application, TaskListView taskListView) {
        this.application = application;
        this.taskListView = taskListView;
    }

    @FXML
    void initialize() throws IOException {
        taskListTitle.textProperty().bindBidirectional(taskListView.getTitle());
        slots = new ArrayList<>();

        for (var slot : taskListView.getSlots()) {
            loadSlot(new SlotController(
                    new AtomicReference<>(taskListView),
                    mainHBox.getChildren(),
                    slots,
                    slot
            ));
        }
    }

    @FXML
    void onNew() {
        confirmationDialog("Are you sure to create new list?", "All unsaved work will be lost", () -> {
            taskListView.getSlots().clear();
            slots.clear();
            mainHBox.getChildren().clear();
            taskListView.getTags().clear();
            taskListTitle.textProperty().set("Untitled");
        });
    }

    @FXML
    void onOpen() throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/openTaskList.fxml"));
        loader.setController(new OpenTaskWindowController(new AtomicReference<>(taskListView)));
        Parent root = loader.load();
        prepareDialog(root, "Open task list").showAndWait();
    }

    @FXML
    void onSave() {

    }

    @FXML
    void onExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export task list");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Task list file", "*.tsk"));
        fileChooser.setInitialFileName(taskListTitle.getText());
        fileChooser.showSaveDialog((Stage) taskListTitle.getScene().getWindow());
    }

    @FXML
    void onCalendar() throws IOException {
        prepareWindow("FXML/calendar.fxml",
                "Calendar view",
                new CalendarViewController(new AtomicReference<>(taskListView)));
    }

    @FXML
    void onSearch() throws IOException {
        prepareWindow("FXML/search.fxml",
                "Search view",
                new SearchWindowController(new AtomicReference<>(taskListView)));
    }

    @FXML
    void onTag() throws IOException {
        prepareWindow("FXML/tagWindow.fxml",
                "Tag view",
                new TagWindowController(new AtomicReference<>(taskListView)));

    }

    @FXML
    void onInfo() {

    }

    @FXML
    void onAddSlot() throws IOException {
        loadSlot(new SlotController(
                new AtomicReference<>(taskListView),
                mainHBox.getChildren(),
                slots
        ));
    }

    private void loadSlot(SlotController slotController) throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/slot.fxml"));
        loader.setController(slotController);
        GridPane newLoadedPane = loader.load();
        slotController.setPane(newLoadedPane);
        slots.add(slotController);
        mainHBox.getChildren().add(newLoadedPane);
        newLoadedPane.setUserData(slotController.getCurrentSlot());
    }

    @FXML
    void onTitleEdit() {
        textInputDialog(taskListTitle.getText(),
                "Edit task list name",
                "Enter new name",
                name -> taskListView.getTitle().setValue(name));
    }
}
