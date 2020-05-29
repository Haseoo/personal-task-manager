package com.github.haseoo.taskmanager.services.adapters;

import com.github.haseoo.taskmanager.data.SlotData;
import com.github.haseoo.taskmanager.data.TagData;
import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.data.TaskListData;
import com.github.haseoo.taskmanager.services.ports.TaskListService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TaskListServiceImpl implements TaskListService {
    private TaskListData taskList;

    public TaskListServiceImpl() {
        loadNewList();
    }

    @Override
    public void loadList(UUID listId) {

    }

    @Override
    public void loadNewList() {
        taskList = TaskListData.defaultNewInstance();

    }

    @Override
    public void saveList() {

    }

    @Override
    public List<TagData> getTags() {
        return taskList.getTags();
    }

    @Override
    public TagData getTagById(UUID id) {
        return taskList.getTags()
                .stream()
                    .filter(tag -> tag.getId().equals(id))
                    .findAny()
                .orElseThrow(AssertionError::new);
    }

    @Override
    public void addTag(TagData tag) {
        taskList.getTags().add(tag);
    }

    @Override
    public void deleteTag(UUID id) {
        getTaskStream()
                .filter(task -> task.getSlot() != null &&
                        task.getSlot().getId().equals(id)
                ).forEach(task -> task.setSlot(null));
        taskList.getSlots().remove(getSlotById(id));
    }

    @Override
    public List<SlotData> getSlots() {
        return taskList.getSlots();
    }

    @Override
    public SlotData getSlotById(UUID id) {
        return taskList.getSlots()
                .stream()
                .filter(slot -> slot.getId().equals(id))
                .findAny()
                .orElseThrow(AssertionError::new);
    }

    @Override
    public void addSlot(SlotData slot) {
        taskList.getSlots().add(slot);
    }

    @Override
    public void deleteSlot(UUID id) {
        taskList.getSlots().remove(getSlotById(id));
    }


    @Override
    public List<TaskData> getTasks() {
        return getTaskStream().collect(toList());
    }

    @Override
    public TaskData getTaskById(UUID id) {
        return getTaskStream()
                .filter(task -> task.getId().equals(id))
                .findAny()
                .orElseThrow(AssertionError::new);
    }

    @Override
    public void addTask(TaskData task) {
        getSlotById(task.getSlot().getId()).getTasks().add(task);
    }

    @Override
    public void removeTask(UUID id) {
        var task = getTaskById(id);
        getSlotById(task.getId()).getTasks().remove(task);
    }


    @Override
    public void moveTask(UUID taskId, UUID slotId) {
        var task = getTaskById(taskId);
        var newSlot = getSlotById(slotId);
        var oldSlot = task.getSlot();
        oldSlot.getTasks().remove(task);
        newSlot.getTasks().add(task);
    }


    private Stream<TaskData> getTaskStream() {
        return taskList.getSlots().stream().flatMap(slot -> slot.getTasks().stream());
    }
}