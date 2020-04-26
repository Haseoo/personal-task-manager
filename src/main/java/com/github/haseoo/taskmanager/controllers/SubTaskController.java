package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.SubTaskView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SubTaskController {
    private final SubTaskView currentSubTask;
    private final Consumer<SubTaskController> onSubTaskDelete;
    private final Runnable updateCompleteness;
    @Getter
    @Setter
    private GridPane subTaskPane;

    @FXML
    private CheckBox subTaskCheck;
    @FXML
    private TextField subTaskInput;

    @FXML
    void initialize() {
        if (currentSubTask.getCompleted().getValue()) {
            subTaskCheck.fire();
        }
        subTaskInput.setText(currentSubTask.getName().getValueSafe());
        subTaskCheck.selectedProperty().addListener(l -> updateCompleteness.run());
    }

    @FXML
    void onSubTaskRemove() {
        onSubTaskDelete.accept(this);
    }

    public boolean isCompleted() {
        return subTaskCheck.isSelected();
    }

    public SubTaskView getCurrentSubTask() {
        return new SubTaskView(new SimpleStringProperty(subTaskInput.getText()),
                new SimpleBooleanProperty(subTaskCheck.isSelected()));
    }

}
