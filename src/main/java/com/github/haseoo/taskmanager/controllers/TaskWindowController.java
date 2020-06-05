package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.SubTaskData;
import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.views.TagView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.haseoo.taskmanager.utilities.DialogStrings.INVALID_TASK_DATE_PROMPT;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.INVALID_TASK_NAME_PROMPT;
import static com.github.haseoo.taskmanager.utilities.Utilities.*;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class TaskWindowController {
    private final TaskData task;
    private final JFXServiceImpl jfxService;

    private final List<SubTaskController> subTaskControllers = new ArrayList<>();

    @FXML
    private TextField nameInput;
    @FXML
    private DatePicker dateFromInput;
    @FXML
    private DatePicker dateToInput;
    @FXML
    Spinner<Integer> positionInput;
    @FXML
    private ComboBox<TagView> tagInput;
    @FXML
    private ProgressBar progress;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private VBox subTaskVBox;
    @FXML
    private TextField keyWordsInput;

    @FXML
    private void initialize() throws IOException {
        nameInput.setText(task.getName());
        dateFromInput.setValue(task.getDateFrom());
        dateToInput.setValue(task.getDateTo());
        descriptionInput.setText(task.getDescription());
        positionInput.setValueFactory(new IntegerSpinnerValueFactory(1,
                jfxService.getCardsInSlotCount(task.getSlot().getId()),
                task.getPosition() + 1));
        tagInput.getItems().addAll(jfxService.getTags().stream().map(TagView::from).collect(toList()));
        if (task.getTag() != null) {
            tagInput.setValue(TagView.from(task.getTag()));
        }
        for (var subTask : task.getSubTasks()) {
            loadSubTask(subTask);
        }
        keyWordsInput.setText(joinKeyWordsToString(task.getKeyWords()));
    }

    @FXML
    private void onSteepAdd() throws IOException {
        loadSubTask(SubTaskData.newInstance());
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() {
        var name = nameInput.getText();
        var dateFrom = dateFromInput.getValue();
        var dateTo = dateToInput.getValue();
        var position = positionInput.getValue() - 1;
        var description = descriptionInput.getText();
        var subTasks = getSubTasks();
        UUID tagId;
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            showUserInputAlert(INVALID_TASK_DATE_PROMPT);
            return;
        }
        if (name != null && !name.equals("")) {
            task.setName(name);
        } else {
            showUserInputAlert(INVALID_TASK_NAME_PROMPT);
            return;
        }
        task.setDateFrom(dateFrom);
        task.setDateTo(dateTo);
        if (tagInput.getSelectionModel().getSelectedIndex() == 0) {
            tagId = null;
        } else {
            tagId = tagInput.getValue().getId();
        }
        jfxService.setTaskTag(task.getId(), tagId);
        if (position != task.getPosition()) {
            jfxService.moveTaskToPosition(task.getId(), position);
        }
        task.setDescription(description);
        task.getSubTasks().clear();
        task.getSubTasks().addAll(subTasks);
        task.getKeyWords().clear();
        task.getKeyWords().addAll(parseKeyWordString(keyWordsInput.getText()));
        jfxService.updateTaskCompletenessOnCard(task.getId(), getCompleteness());
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) nameInput.getScene().getWindow()).close();
    }

    private void loadSubTask(SubTaskData subTask) throws IOException {
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.SUB_TASK));
        var controller = new SubTaskController(subTask,
                subTaskVBox.getChildren()::remove,
                this::updateTaskCompleteness);
        loader.setController(controller);
        subTaskControllers.add(controller);
        subTaskVBox.getChildren().add(loader.load());
        updateTaskCompleteness();
    }

    private void updateTaskCompleteness() {
        progress.progressProperty().set(getCompleteness());
    }

    private List<SubTaskData> getSubTasks() {
        return subTaskControllers.stream()
                .filter(SubTaskController::isPresent)
                .map(SubTaskController::getSubTask)
                .collect(toList());
    }

    private double getCompleteness() {
        double subTaskCount = subTaskControllers.stream()
                .filter(SubTaskController::isPresent)
                .count();
        double completeSubtaskCount = subTaskControllers.stream()
                .filter(SubTaskController::isPresent)
                .filter(SubTaskController::isComplete)
                .count();
        return completeSubtaskCount / subTaskCount;
    }
}
