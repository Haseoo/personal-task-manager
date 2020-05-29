package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TagData;
import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.DialogStrings.TASK_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.TASK_MOVE_DIALOG_TITLE_FORMAT;
import static com.github.haseoo.taskmanager.utilities.Utilities.*;

@RequiredArgsConstructor
public class TaskController {
    private final JFXServiceImpl jfxService;
    private final TaskData currentTask;

    @Setter
    @Getter
    private Node currentCard;
    private Node taskInfoNode;

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

    private final ChangeListener<String> tagNameChangeListener = (observable, oldVal, newVal) -> taskTag.setText(newVal);

    private final ChangeListener<TagData> tagDataChangeListener = (observable, oldVal, newVal) -> {
        oldVal.unbindName(taskTag.textProperty());
        taskTag.setText(newVal.getName());
        newVal.bindName(taskTag.textProperty());
    };

    @FXML
    void initialize() {
        taskInfoNode = rightGrid.getChildren()
                .stream()
                .filter(e -> e.getId().equals("moreInfo")).findAny()
                .orElseThrow(AssertionError::new);
        rightGrid.getChildren().remove(taskInfoNode);
        currentTask.bindName(taskLabel.textProperty());
        currentTask.bindDateFrom(taskFromDate.textProperty());
        currentTask.bindDateTo(taskToDate.textProperty());
        if (currentTask.getTag() != null) {
            currentTask.getTag().bindName(taskTag.textProperty());
        }
        currentTask.addTagListener(tagDataChangeListener);
    }

    @FXML
    private void onTaskMove() throws IOException {
        var controller = new MoveTaskDialogController(jfxService, currentTask);
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.MOVE_TASK_DIALOG));
        loader.setController(controller);
        prepareDialog(loader.load(),
                String.format(TASK_MOVE_DIALOG_TITLE_FORMAT, currentTask.getName()))
                .showAndWait();
        controller.getResponse().ifPresent(response -> jfxService
                .moveTaskToSlot(currentTask.getId(),
                        response.getSlotId(),
                        response.getPosition()));
    }

    @FXML
    private void onTaskDelete() {
        deleteConfirmationDialog(
                String.format(TASK_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT, currentTask.getName()),
                () -> jfxService.removeTask(currentTask.getId())
        );
    }

    @FXML
    private void onClick() {
    }

    @FXML
    void hideInfo() {
        rightGrid.getChildren().remove(taskInfoNode);
    }

    @FXML
    void showInfo() {
        rightGrid.add(taskInfoNode, 0, 1);
    }

}
