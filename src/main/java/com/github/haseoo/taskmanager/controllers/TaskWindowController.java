package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.SubTaskView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TagView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;


@RequiredArgsConstructor
public class TaskWindowController {
    private final TaskView currentTask;
    private final int maxPosition;
    private final AtomicReference<List<TagView>> tags;
    private final IntConsumer onTaskMove;

    private List<SubTaskController> subTasks;

    @FXML
    private TextField nameInput;
    @FXML
    private DatePicker dateFromInput;
    @FXML
    private DatePicker dateToInput;
    @FXML
    Spinner<Integer> positionInput;
    @FXML
    private ComboBox<String> tagInput;
    @FXML
    private ProgressBar progress;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private VBox subTaskVBox;

    @FXML
    void initialize() throws IOException {
        nameInput.setText(currentTask.getName().getValueSafe());
        var tagInputItems = tagInput.getItems();
        for (var tag : tags.get()) {
            tagInputItems.add(tag.getName().getValueSafe());
        }
        if (!currentTask.getDateFrom().getValueSafe().isEmpty()) {
            dateFromInput.setValue(LocalDate.parse(currentTask.getDateFrom().getValueSafe(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (!currentTask.getDateTo().getValueSafe().isEmpty()) {
            dateToInput.setValue(LocalDate.parse(currentTask.getDateTo().getValueSafe(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (!currentTask.getTagName().getValueSafe().isEmpty()) {
            tagInput.setValue(currentTask.getTagName().getValueSafe());
        }
        descriptionInput.setText(currentTask.getDescription().getValueSafe());
        if (onTaskMove != null) {
            positionInput.setValueFactory(new IntegerSpinnerValueFactory(1,
                    maxPosition,
                    currentTask.getPosition().getValue() + 1));
        } else {
            positionInput.setDisable(true);
        }
        subTasks = new ArrayList<>();
        for (var subTask : currentTask.getSubTasks()) {
            loadTask(subTask);
        }
        updateCompleteness();
    }

    @FXML
    void onSteepAdd() throws IOException {
        SubTaskView newSubTask = new SubTaskView(new SimpleStringProperty("new task"),
                new SimpleBooleanProperty(false));
        loadTask(newSubTask);
        updateCompleteness();
    }

    @FXML
    void onCancel() {
        closeWindow();
    }

    @FXML
    void onSave() {
        var newDateFrom = dateFromInput.getValue();
        var newDateTo = dateToInput.getValue();
        if (newDateTo != null) {
            currentTask.getDateTo().setValue(newDateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (newDateFrom != null && newDateTo.isBefore(newDateFrom)) {
                dateToInput.setStyle("-fx-background-color:red;");
                return;
            }
        }
        currentTask.getName().setValue(nameInput.getText());
        var newTag = tagInput.getValue();
        currentTask.getTagName().setValue((newTag.equals("None") ? "" : newTag));
        currentTask.getDescription().setValue(descriptionInput.getText());
        if (newDateFrom != null) {
            currentTask.getDateFrom().setValue(newDateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (onTaskMove != null) {
            var newPosition = positionInput.getValue() - 1;
            if (currentTask.getPosition().get() != newPosition) {
                onTaskMove.accept(newPosition);
            }
        }
        var currentSubTasks = currentTask.getSubTasks();
        currentSubTasks.clear();
        for (var subTask : subTasks) {
            currentSubTasks.add(subTask.getCurrentSubTask());
        }
        if (subTasks.isEmpty()) {
            currentTask.getCompleteness().set("");
        } else {
            var percentageValue = progress.getProgress() * 100;
            currentTask.getCompleteness().set(String.format("%.2f%%", percentageValue));
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) nameInput.getScene().getWindow()).close();
    }

    void loadTask(SubTaskView subTaskView) throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/subTask.fxml"));
        var controller = new SubTaskController(subTaskView, this::deleteSubTask, this::updateCompleteness);
        loader.setController(controller);
        GridPane newLoadedPane = loader.load();
        controller.setSubTaskPane(newLoadedPane);
        subTasks.add(controller);
        subTaskVBox.getChildren().add(newLoadedPane);
    }

    private void deleteSubTask(SubTaskController subTask) {
        subTaskVBox.getChildren().remove(subTask.getSubTaskPane());
        subTasks.remove(subTask);
        updateCompleteness();
    }

    private void updateCompleteness() {
        var subTaskCount = subTasks.size();
        if (subTaskCount == 0) {
            progress.progressProperty().set(0);
            return;
        }
        var completedSubTasks = subTasks.stream().filter(SubTaskController::isCompleted).count();
        var progressValue = (double) completedSubTasks / (double) subTaskCount;
        progress.progressProperty().set(progressValue);
    }
}
