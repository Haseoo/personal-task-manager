package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.SlotComboBoxView;
import com.github.haseoo.taskmanager.controllers.views.taskList.SlotView;
import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import com.github.haseoo.taskmanager.controllers.views.taskList.TaskView;
import com.github.haseoo.taskmanager.utilities.Utilities;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;
import static java.util.stream.Collectors.toList;

public class TaskController {
    private final AtomicReference<TaskListView> taskListView;
    @Setter
    private List<Node> otherTaskCards;
    @Setter
    private List<TaskController> otherTasks;

    private Node taskInfoNode;
    @Getter
    private TaskView currentTask;
    @Setter
    private BiConsumer<TaskController, UUID> moveTask;
    @Setter
    @Getter
    private Node currentCard;
    @FXML
    private GridPane rightGrid;
    @FXML
    private Label taskLabel;
    @FXML
    private Text taskFromDate;
    @FXML
    private Text taskToDate;
    @FXML
    private Text taskCompleteness;
    @FXML
    private Label taskTag;

    public TaskController(AtomicReference<TaskListView> taskListView,
                          List<Node> otherTaskCards,
                          List<TaskController> otherTasks) {
        this.taskListView = taskListView;
        this.otherTaskCards = otherTaskCards;
        this.otherTasks = otherTasks;
        currentTask = new TaskView(UUID.randomUUID(),
                new SimpleStringProperty("New Task"),
                new SimpleStringProperty(null),
                new SimpleStringProperty(null),
                new SimpleStringProperty(null),
                new SimpleStringProperty(null),
                new SimpleStringProperty(null),
                new SimpleIntegerProperty(otherTaskCards.size()),
                new ArrayList<>());
    }

    @FXML
    void initialize() {
        taskInfoNode = rightGrid.getChildren()
                .stream()
                .filter(e -> e.getId().equals("moreInfo")).findAny()
                .orElseThrow(AssertionError::new);
        rightGrid.getChildren().remove(taskInfoNode);
        taskLabel.textProperty().bindBidirectional(currentTask.getName());
        taskFromDate.textProperty().bindBidirectional(currentTask.getDateFrom());
        taskToDate.textProperty().bindBidirectional(currentTask.getDateTo());
        taskCompleteness.textProperty().bindBidirectional(currentTask.getCompleteness());
        taskTag.textProperty().bindBidirectional(currentTask.getTagName());
    }

    @FXML
    void onTaskMove() throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/moveTask.fxml"));
        var slots = taskListView.get()
                .getSlots().stream()
                .map(SlotComboBoxView::from)
                .collect(toList());
        var currentSlotName = taskListView.get().getSlots()
                .stream()
                .filter(slot -> slot.getTasks().contains(currentTask))
                .map(SlotComboBoxView::from)
                .findAny().orElseThrow(AssertionError::new);
        var controller = new moveTaskDialogController(currentSlotName, slots);
        loader.setController(controller);
        Parent root = loader.load();
        prepareDialog(root, String.format("Move task %s", currentTask.getName().getValueSafe())).showAndWait();
        controller.getResponse().ifPresent(newSlot -> moveTask.accept(this, newSlot.getId()));
    }

    @FXML
    void onTaskDelete() {
        deleteConfirmationDialog(String.format("Are you sure to delete task %s?", currentTask.getName().getValueSafe()), () -> {
            otherTaskCards.remove(currentCard);
            updateCardPosition();
        });
    }

    @FXML
    void showInfo() {
        rightGrid.add(taskInfoNode, 0, 1);
    }

    @FXML
    void onClick() throws IOException {
        prepareWindow("FXML/taskWindow.fxml",
                currentTask.getName().getValueSafe(),
                new TaskWindowController(currentTask,
                        otherTasks.size(),
                        new AtomicReference<>(taskListView.get().getTags()),
                        this::moveTask));
    }

    @FXML
    void hideInfo() {
        rightGrid.getChildren().remove(taskInfoNode);
    }

    private void updateCardPosition() {
        for(var task : otherTasks) {
            task.currentTask.getPosition().setValue(otherTaskCards.indexOf(task.currentCard));
        }
    }

    private void moveTask(int newPosition) {
        otherTaskCards.remove(currentCard);
        otherTaskCards.add(newPosition, currentCard);
        updateCardPosition();
    }
}
