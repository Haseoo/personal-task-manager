package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.TaskTemplateData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
public class TaskTemplate {
    private UUID id;
    private String taskName;
    private boolean hasDuration;
    private int duration;
    private List<String> keyWords;
    private List<String> subTaskNames;

    public static TaskTemplate from(TaskTemplateData taskTemplateData) {
        return new TaskTemplate(taskTemplateData.getId(),
                taskTemplateData.getTaskName(),
                taskTemplateData.isHasDuration(),
                taskTemplateData.getDuration(),
                taskTemplateData.getKeyWords(),
                taskTemplateData.getSubTaskNames());
    }
}
