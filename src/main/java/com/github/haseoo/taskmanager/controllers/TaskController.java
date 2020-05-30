package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TagColorData;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Constants.*;
import static com.github.haseoo.taskmanager.utilities.Converters.tagColorToFfxColor;
import static com.github.haseoo.taskmanager.utilities.DefaultValues.DEFAULT_TAG_COLOR;
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
    @FXML
    private GridPane mainPane;

    private final ChangeListener<String> tagNameChangeListener = (observable, oldVal, newVal) -> taskTag.setText(newVal);

    private final ChangeListener<TagColorData> tagColorChangeListener = (observable, oldVal, newVal) -> applyNewColor(newVal);

    private final ChangeListener<TagData> tagDataChangeListener = (observable, oldVal, newVal) -> {
        if (oldVal != null) {
            oldVal.unbindName(taskTag.textProperty());
            oldVal.removeColorListener(tagColorChangeListener);
        }
        if (newVal != null) {
            taskTag.setText(newVal.getName());
            applyNewColor(newVal.getTagColor());
            newVal.bindName(taskTag.textProperty());
            newVal.addColorListener(tagColorChangeListener);
        } else {
            taskTag.setText("");
            applyNewColor(DEFAULT_TAG_COLOR);
        }
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
            currentTask.getTag().addColorListener(tagColorChangeListener);
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
        currentTask.setTag(jfxService.getTags().get(0));
    }

    @FXML
    void hideInfo() {
        mainPane.setStyle(CARD_STYLE);
        rightGrid.getChildren().remove(taskInfoNode);
    }

    @FXML
    void showInfo() {
        TagColorData tagColor = calculateBgColor();
        mainPane.setStyle(String.format(HOVER_CARD_STYLE_FORMAT, tagColor.getRed(), tagColor.getGreen(), tagColor.getBlue()));
        rightGrid.add(taskInfoNode, 0, 1);
    }

    private TagColorData calculateBgColor() {
        Color jfxTagColor;
        if (currentTask.getTag() == null) {
            jfxTagColor = tagColorToFfxColor(DEFAULT_TAG_COLOR);
        } else {
            jfxTagColor = tagColorToFfxColor(currentTask.getTag().getTagColor());
        }
        jfxTagColor = Color.hsb(jfxTagColor.getHue(), jfxTagColor.getSaturation() / 3.5, jfxTagColor.getBrightness());
        return TagColorData.newInstance(jfxTagColor);
    }

    private void applyNewColor(TagColorData newVal) {
        taskLabel.setStyle(String.format(CARD_TITLE_STYLE_FORMAT, newVal.getRed(), newVal.getGreen(), newVal.getBlue()));
    }
}
