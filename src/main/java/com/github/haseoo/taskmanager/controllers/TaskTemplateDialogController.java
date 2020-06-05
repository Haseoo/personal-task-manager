package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskTemplateData;
import com.github.haseoo.taskmanager.views.TaskTemplateView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class TaskTemplateDialogController {
    private final List<TaskTemplateData> templates;

    @FXML
    private ComboBox<TaskTemplateView> templateInput;

    private UUID response;

    @FXML
    private void initialize() {
        templateInput.getItems().addAll(templates.stream().map(TaskTemplateView::form).collect(toList()));
    }

    public Optional<UUID> getResponse() {
        return Optional.ofNullable(response);
    }

    @FXML
    private void onOk() {
        var selected = templateInput.getSelectionModel();
        if (selected != null) {
            response = selected.getSelectedItem().getId();
        }
        ((Stage) templateInput.getScene().getWindow()).close();
    }
}
