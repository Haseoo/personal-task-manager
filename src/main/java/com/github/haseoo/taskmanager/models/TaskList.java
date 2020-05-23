package com.github.haseoo.taskmanager.models;

import lombok.Data;

import java.util.List;

@Data
public class TaskList {
    private String name;
    private List<Slot> slots;
    private List<Task> tasks;
    private List<Tag> tags;
}
