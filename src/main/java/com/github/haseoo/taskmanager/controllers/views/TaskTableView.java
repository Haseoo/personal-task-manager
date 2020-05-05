package com.github.haseoo.taskmanager.controllers.views;

import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskView;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TaskTableView {
    UUID id;
    String taskName;
    String fromDate;
    String toDate;
    String taskTagName;
    String completeness;

    public static TaskTableView from(TaskView taskView) {
        return new TaskTableView(taskView.getId(),
                taskView.getName().getValueSafe(),
                taskView.getDateFrom().getValueSafe(),
                taskView.getDateTo().getValueSafe(),
                taskView.getTagName().getValueSafe(),
                taskView.getCompleteness().getValueSafe());
    }
}
