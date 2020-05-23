package com.github.haseoo.taskmanager.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskList {
    private List<Slot> categories;

    public static TaskList newInstance() {
        TaskList taskList = new TaskList();
        taskList.categories = new ArrayList<>();
        return taskList;
    }
}
