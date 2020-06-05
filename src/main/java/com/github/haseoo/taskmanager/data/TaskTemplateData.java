package com.github.haseoo.taskmanager.data;

import com.github.haseoo.taskmanager.controllers.TaskTemplateController;
import com.github.haseoo.taskmanager.models.TaskTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public final class TaskTemplateData {
    private final UUID id;
    private String taskName;
    private boolean hasDuration;
    private int duration;
    private final List<String> keyWords;
    private final List<String> subTaskNames;

    public static TaskTemplateData from(TaskTemplateController.Response response) {
        return new TaskTemplateData(UUID.randomUUID(),
                response.getName(),
                response.isHasDuration(),
                response.getDaysToFinish(),
                new ArrayList<>(response.getKeyWords()),
                new ArrayList<>(response.getSubTaskNames()));
    }

    public static TaskTemplateData from(TaskTemplate taskTemplate) {
        return new TaskTemplateData(taskTemplate.getId(),
                taskTemplate.getTaskName(),
                taskTemplate.isHasDuration(),
                taskTemplate.getDuration(),
                new ArrayList<>(taskTemplate.getKeyWords()),
                new ArrayList<>(taskTemplate.getSubTaskNames()));
    }
}
