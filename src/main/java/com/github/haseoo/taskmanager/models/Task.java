package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.TaskData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    private UUID id;
    private String name;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String description;
    private int position;

    private UUID slotId;
    private UUID tagId;

    private List<SubTask> subTasks;
    private List<String> keyWords;

    public static Task form(TaskData taskData) {
        return new Task(taskData.getId(),
                taskData.getName(),
                taskData.getDateFrom(),
                taskData.getDateTo(),
                taskData.getDescription(),
                taskData.getPosition(),
                taskData.getSlot().getId(),
                (taskData.getTag() != null) ? taskData.getTag().getId() : null,
                taskData.getSubTasks().stream().map(SubTask::from).collect(toList()),
                taskData.getKeyWords());
    }
}
