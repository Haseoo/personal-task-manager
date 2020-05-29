package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.SubTaskData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubTask {
    private String name;
    private boolean complete;

    public static SubTask from(SubTaskData subTaskData) {
        return new SubTask(subTaskData.getName(), subTaskData.isComplete());
    }
}
