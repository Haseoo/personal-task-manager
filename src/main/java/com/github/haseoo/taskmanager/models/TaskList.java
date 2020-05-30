package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.TaskListData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
public class TaskList {
    private UUID id;
    private String name;
    private List<Slot> slots;
    private List<Task> tasks;
    private List<Tag> tags;

    public static TaskList from(TaskListData taskListData) {
        return new TaskList(taskListData.getId(),
                taskListData.getName(),
                taskListData.getSlots().stream().map(Slot::form).collect(toList()),
                taskListData.getSlots().stream().flatMap(slot -> slot.getTasks().stream()).map(Task::form).collect(toList()),
                taskListData.getTags().stream().map(Tag::from).collect(toList()));
    }
}
