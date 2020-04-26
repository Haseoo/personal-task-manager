package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TagView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;


@RequiredArgsConstructor
public class TaskWindowController {
    private final TaskView currentTask;
    private final int maxPosition;
    private final AtomicReference<List<TagView>> tags;
    private final IntConsumer onTaskMove;

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
    void initialize() {
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
        if (!currentTask.getCompleteness().getValueSafe().isEmpty()) {
            progress.progressProperty().set(Double.parseDouble(currentTask.getCompleteness().getValue()) / 100.0);
        }
        if (!currentTask.getTagName().getValueSafe().isEmpty()) {
            tagInput.setValue(currentTask.getTagName().getValueSafe());
        }
        descriptionInput.setText(currentTask.getDescription().getValueSafe());
        positionInput.setValueFactory(new IntegerSpinnerValueFactory(1,
                maxPosition,
                currentTask.getPosition().getValue() + 1));
    }

    @FXML
    void onSteepAdd() {

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
        var newPosition = positionInput.getValue() - 1;
        if (currentTask.getPosition().get() != newPosition) {
            onTaskMove.accept(newPosition);
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) nameInput.getScene().getWindow()).close();
    }
}
