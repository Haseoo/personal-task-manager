package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
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
                    this::onSlotRemove,
                    this::onSlotMove,
                    mainHBox.getChildren()::size,
                    slot
            ));
        }
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
                this::onSlotRemove,
                this::onSlotMove,
                mainHBox.getChildren()::size,
                mainHBox.getChildren().size()
        ));
    }

    private void loadSlot(SlotController slotController) throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/slot.fxml"));
        loader.setController(slotController);
        GridPane newLoadedPane = loader.load();
        slotController.setPane(newLoadedPane);
        slots.add(slotController);
        mainHBox.getChildren().add(newLoadedPane);
    }

    @FXML
    void onTitleEdit() {
        textInputDialog(taskListTitle.getText(),
                "Edit task list name",
                "Enter new name",
                name -> taskListView.getTitle().setValue(name));
    }

    private void onSlotRemove(SlotController slot) {
        mainHBox.getChildren().remove(slot.getCurrentPane());
        slots.remove(slot);
        updateSlotPositions();
    }

    private void onSlotMove(SlotController slot) {
        int newIndex = slot.getCurrentSlot().getPosition().getValue();
        mainHBox.getChildren().remove(slot.getCurrentPane());
        mainHBox.getChildren().add(newIndex, slot.getCurrentPane());
        updateSlotPositions();
    }

    private void updateSlotPositions() {
        for (var slot : slots) {
            slot.updatePosition(mainHBox.getChildren().indexOf(slot.getCurrentPane()));
        }
    }
}
