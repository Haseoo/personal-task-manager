package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.TableViewCallback;
import com.github.haseoo.taskmanager.views.TagView;
import com.github.haseoo.taskmanager.views.TaskStatusComboBox;
import com.github.haseoo.taskmanager.views.TaskView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SearchWindowController {
    private final JFXServiceImpl jfxService;
    @FXML
    private ComboBox<TagView> tagInput;

    @FXML
    private ComboBox<String> statusInput;

    @FXML
    private DatePicker fromDateInput;

    @FXML
    private DatePicker toDateInput;

    @FXML
    private TextField taskNameInput;

    @FXML
    private TableView<TaskView> resultTable;

    private static Map<TaskStatusComboBox, Predicate<TaskData>> statusPredicates;

    static {
        statusPredicates = new EnumMap<>(TaskStatusComboBox.class);
        statusPredicates.put(TaskStatusComboBox.ALL, task -> true);
        statusPredicates.put(TaskStatusComboBox.DELAYED,
                task -> task.getDateTo() != null && task.getDateTo().isBefore(LocalDate.now()));
        statusPredicates.put(TaskStatusComboBox.URGENT,
                task -> {
                    if (task.getDateTo() == null) {
                        return false;
                    }
                    var daysBetween = ChronoUnit.DAYS.between(task.getDateTo(), LocalDate.now());
                    return (Double.isNaN(task.getCompleteness()) ||
                            task.getCompleteness() < 100.0) &&
                            daysBetween >= 0 && daysBetween < 2;
                });
    }

    @FXML
    private void initialize() {
        tagInput.getItems().addAll(jfxService.getTags().stream().map(TagView::from).collect(toList()));
        resultTable.setRowFactory(new TableViewCallback(jfxService));
    }

    @FXML
    private void onSearch() {
        var condition = prepareCondition();
        resultTable.getItems().clear();
        var items = jfxService.getTasksByCondition(condition);
        var mapped = items.stream().map(TaskView::from).collect(toList());
        resultTable.getItems().addAll(mapped);
    }

    @FXML
    private void onReset() {
        statusInput.setValue(statusInput.getItems().get(0));
        fromDateInput.setValue(null);
        toDateInput.setValue(null);
        taskNameInput.setText(null);
    }

    private Predicate<TaskData> prepareCondition() {
        var tag = (tagInput.getSelectionModel().getSelectedIndex() != 0) ?
                tagInput.getSelectionModel().getSelectedItem() :
                null;
        var status = TaskStatusComboBox.from(statusInput.getSelectionModel().getSelectedItem());
        var dateTo = toDateInput.getValue();
        var dateFrom = fromDateInput.getValue();
        var name = taskNameInput.getText();
        Predicate<TaskData> predicate = statusPredicates.get(status);
        if (name != null && !name.equals("")) {
            predicate = predicate.and(task -> task.getName().contains(name));
        }
        if (tag != null) {
            predicate = predicate.and(task -> task.getTag() != null && task.getTag().getId().equals(tag.getId()));
        }
        if (dateFrom != null) {
            predicate = predicate.and(task -> task.getDateFrom() != null && task.getDateFrom().equals(dateFrom));
        }
        if (dateTo != null) {
            predicate = predicate.and(task -> task.getDateTo() != null && task.getDateTo().equals(dateTo));
        }
        return predicate;
    }
}
