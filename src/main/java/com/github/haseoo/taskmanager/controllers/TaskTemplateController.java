package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskTemplateData;
import com.github.haseoo.taskmanager.utilities.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static com.github.haseoo.taskmanager.utilities.DialogStrings.TASK_TEMPLATE_ADD_SUBTASK_TEXT;
import static com.github.haseoo.taskmanager.utilities.Utilities.joinKeyWordsToString;
import static com.github.haseoo.taskmanager.utilities.Utilities.parseKeyWordString;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
public class TaskTemplateController {
    private final TaskTemplateData taskTemplateData;

    @FXML
    private TextField nameInput;

    @FXML
    private CheckBox hasDuration;

    @FXML
    private Spinner<Integer> daysToFinishInput;

    @FXML
    private TextField keyWordsInput;

    @FXML
    private ListView<String> subTasks;

    private Response response;

    @FXML
    private void initialize() {
        hasDuration.selectedProperty().addListener((observable, oldVal, newVal) -> daysToFinishInput.setDisable(!newVal));

        if (taskTemplateData != null) {
            nameInput.setText(taskTemplateData.getTaskName());
            hasDuration.setSelected(taskTemplateData.isHasDuration());
            daysToFinishInput.setValueFactory(new IntegerSpinnerValueFactory(0,
                    Integer.MAX_VALUE,
                    taskTemplateData.getDuration()));
            subTasks.getItems().addAll(taskTemplateData.getSubTaskNames());
            keyWordsInput.setText(joinKeyWordsToString(taskTemplateData.getKeyWords()));
        } else {
            daysToFinishInput.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        }
    }

    public Optional<Response> getResponse() {
        return Optional.ofNullable(response);
    }

    @FXML
    private void onSubTaskAdd() {
        Utilities.textInputDialog("",
                TASK_TEMPLATE_ADD_SUBTASK_TEXT,
                TASK_TEMPLATE_ADD_SUBTASK_TEXT,
                subTasks.getItems()::add);
    }

    @FXML
    private void onSubTaskRemove() {
        var selected = subTasks.getSelectionModel();
        if (selected != null) {
            subTasks.getItems().remove(selected.getSelectedItem());
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() {
        response = new Response(nameInput.getText(),
                hasDuration.isSelected(),
                (hasDuration.isSelected()) ? daysToFinishInput.getValue() : 0,
                parseKeyWordString(keyWordsInput.getText()),
                subTasks.getItems());
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) nameInput.getScene().getWindow()).close();
    }

    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Response {
        String name;
        boolean hasDuration;
        int daysToFinish;
        List<String> keyWords;
        List<String> subTaskNames;
    }
}
