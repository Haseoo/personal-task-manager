package com.github.haseoo.taskmanager.views;

import com.github.haseoo.taskmanager.data.TaskData;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TaskView {
    UUID id;
    String taskName;
    String fromDate;
    String toDate;
    String taskTagName;
    String completeness;

    public static TaskView from(TaskData taskData) {
        return new TaskView(taskData.getId(),
                taskData.getName(),
                (taskData.getDateFrom() != null) ? taskData.getDateFrom().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) : "",
                (taskData.getDateTo() != null) ? taskData.getDateTo().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) : "",
                (taskData.getTag() != null) ? taskData.getTag().getName() : "",
                getTaskCompletenessString(taskData));
    }

    private static String getTaskCompletenessString(TaskData taskData) {
        return (Double.isNaN(taskData.getCompleteness()) ? "" : Double.toString(taskData.getCompleteness()));
    }
}
