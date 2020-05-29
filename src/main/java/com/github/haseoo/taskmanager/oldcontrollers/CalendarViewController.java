package com.github.haseoo.taskmanager.oldcontrollers;

import com.github.haseoo.taskmanager.oldcontrollers.views.TaskTableView;
import com.github.haseoo.taskmanager.oldcontrollers.views.tasklist.SlotView;
import com.github.haseoo.taskmanager.oldcontrollers.views.tasklist.TaskListView;
import com.github.haseoo.taskmanager.oldcontrollers.views.tasklist.TaskView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.prepareWindow;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class CalendarViewController {

    private final AtomicReference<TaskListView> taskListView;

    @FXML
    private DatePicker dateInput;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TableView<TaskTableView> resultTable;
    @FXML
    private Button refreshBtn;

    @FXML
    void initialize() {
        dateInput.valueProperty().addListener(this::onDateUpdate);
        type.valueProperty().addListener(this::onTypeUpdate);
        resultTable.setRowFactory(tv -> {
            TableRow<TaskTableView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TaskTableView rowData = row.getItem();
                    loadTaskWindow(rowData.getId());
                }
            });
            return row;
        });
    }

    @FXML
    void onRefresh() {
        updateTable();
    }


    private void onDateUpdate(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
        refreshBtn.setDisable(newValue == null);
        type.setDisable(newValue == null);
        updateTable();

    }

    private void onTypeUpdate(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        updateTable();

    }

    private void loadTaskWindow(UUID taskId) {
        var currentTask = taskListView
                .get()
                .getSlots()
                .stream()
                .map(SlotView::getTasks)
                .flatMap(List::stream)
                .filter(task -> task.getId().equals(taskId))
                .findAny()
                .orElseThrow(AssertionError::new);

        try {
            prepareWindow("FXML/taskWindow.fxml",
                    currentTask.getName().getValueSafe(),
                    new TaskWindowController(currentTask,
                            0,
                            new AtomicReference<>(taskListView.get().getTags()),
                            null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTable() {
        var tasks = taskListView
                .get()
                .getSlots()
                .stream()
                .map(SlotView::getTasks)
                .flatMap(List::stream)
                .filter(this::getFilterCondition)
                .map(TaskTableView::from)
                .collect(toList());
        resultTable.getItems().clear();
        resultTable.getItems().addAll(tasks);
    }

    private boolean getFilterCondition(TaskView taskView) {
        var selectedDate = dateInput.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        switch (type.getSelectionModel().getSelectedIndex()) {
            case 0:
                return taskView.getDateFrom().getValueSafe().equals(selectedDate) ||
                        taskView.getDateTo().getValueSafe().equals(selectedDate);
            case 1:
                return taskView.getDateFrom().getValueSafe().equals(selectedDate);
            case 2:
                return taskView.getDateTo().getValueSafe().equals(selectedDate);
            default:
                throw new AssertionError();
        }
    }
}
