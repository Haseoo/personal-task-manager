package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.SubTaskData;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SubTaskController {
    @Getter
    private final SubTaskData subTask;
    private final Consumer<? super GridPane> onSubTaskRemove;
    private final Runnable updateCompleteness;
    @FXML
    private CheckBox subTaskCheck;
    @FXML
    private TextField subTaskInput;
    @FXML
    private GridPane subTaskPane;
    @Getter
    private boolean present = true;

    @FXML
    private void initialize() {
        subTask.bindName(subTaskInput.textProperty());
        subTask.bindComplete(subTaskCheck.selectedProperty());
        subTaskCheck.selectedProperty().addListener(o -> updateCompleteness.run());
    }

    public boolean isComplete() {
        return subTaskCheck.isSelected();
    }

    @FXML
    private void onSubTaskRemove() {
        onSubTaskRemove.accept(subTaskPane);
        present = false;
        updateCompleteness.run();
    }

}
