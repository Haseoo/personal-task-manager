package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskTemplateData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.views.TaskTemplateView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.DefaultValues.DEFAULT_TASK_TEMPLATE_NAME;
import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;
import static com.github.haseoo.taskmanager.utilities.Utilities.prepareDialog;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class TaskTemplateWindowController {
    private final JFXServiceImpl jfxService;

    @FXML
    private void initialize() {
        updateTable();
    }

    @FXML
    private TableView<TaskTemplateView> table;

    @FXML
    private void onAdd() throws IOException {
        TaskTemplateController controller = loadDialog(null);
        controller.getResponse().ifPresent(response -> {
            jfxService.addTaskTemplate(TaskTemplateData.from(response));
            updateTable();
        });
    }

    @FXML
    private void onEdit() throws IOException {
        var selected = table.getSelectionModel();
        if (selected != null && selected.getSelectedItem() != null) {
            var template = jfxService.getTemplateById(selected.getSelectedItem().getId());
            TaskTemplateController controller = loadDialog(template);
            controller.getResponse().ifPresent(response -> {
                updateTaskTemplate(template, response);
                updateTable();
            });
        }
    }

    @FXML
    private void onRemove() {
        var selected = table.getSelectionModel();
        if (selected != null) {
            jfxService.removeTaskTemplate(selected.getSelectedItem().getId());
        }
    }

    void updateTable() {
        table.getItems().clear();
        table.getItems().addAll(jfxService.getTaskTemplates()
                .stream()
                .map(TaskTemplateView::form)
                .collect(toList()));
    }

    private void updateTaskTemplate(TaskTemplateData templateData, TaskTemplateController.Response response) {
        templateData.setTaskName(response.getName());
        templateData.setHasDuration(response.isHasDuration());
        templateData.setDuration(response.getDaysToFinish());
        templateData.getKeyWords().clear();
        templateData.getKeyWords().addAll(response.getKeyWords());
        templateData.getSubTaskNames().clear();
        templateData.getSubTaskNames().addAll(response.getSubTaskNames());
    }

    private TaskTemplateController loadDialog(TaskTemplateData taskTemplateData) throws IOException {
        var controller = new TaskTemplateController(taskTemplateData);
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.TASK_TEMPLATE));
        loader.setController(controller);
        prepareDialog(loader.load(),
                DEFAULT_TASK_TEMPLATE_NAME)
                .showAndWait();
        return controller;
    }
}
