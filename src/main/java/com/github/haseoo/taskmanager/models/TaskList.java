package com.github.haseoo.taskmanager.models;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TaskList {
    private UUID id;
    private String name;
    private List<Slot> slots;
    private List<Task> tasks;
    private List<Tag> tags;
}
