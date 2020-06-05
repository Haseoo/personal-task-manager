package com.github.haseoo.taskmanager.views;

import com.github.haseoo.taskmanager.data.TaskTemplateData;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TaskTemplateView {
    UUID id;
    String name;

    @Override
    public String toString() {
        return name;
    }

    public static TaskTemplateView form(TaskTemplateData taskTemplateData) {
        return new TaskTemplateView(taskTemplateData.getId(), taskTemplateData.getTaskName());
    }
}
