package com.github.haseoo.taskmanager.utilities;

import com.github.haseoo.taskmanager.controllers.TaskWindowController;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.views.TaskView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.UUID;

import static com.github.haseoo.taskmanager.utilities.Utilities.prepareWindow;

@AllArgsConstructor
public final class TableViewCallback implements Callback<TableView<TaskView>, TableRow<TaskView>> {
    private final JFXServiceImpl jfxService;

    @Override
    public TableRow<TaskView> call(TableView<TaskView> tv) {
        TableRow<TaskView> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                TaskView rowData = row.getItem();
                loadTaskWindow(rowData.getId());
            }
        });
        return row;
    }

    @SneakyThrows
    private void loadTaskWindow(UUID taskId) {
        var selectedTask = jfxService.getTaskById(taskId);
        prepareWindow(FxmlFilePaths.TASK_WINDOW,
                selectedTask.getName(),
                new TaskWindowController(selectedTask, jfxService));
    }
}
