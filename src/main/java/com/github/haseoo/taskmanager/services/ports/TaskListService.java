package com.github.haseoo.taskmanager.services.ports;


import com.github.haseoo.taskmanager.data.*;
import com.github.haseoo.taskmanager.models.TaskList;

import java.util.List;
import java.util.UUID;

public interface TaskListService {
    void loadNewList();

    TaskList prepareModels();

    TaskListData getCurrentTaskList();

    List<TagData> getTags();

    TagData getTagById(UUID id);

    void addTag(TagData tag);

    void deleteTag(UUID id);

    List<SlotData> getSlots();

    SlotData getSlotById(UUID id);

    void addSlot(SlotData slot);

    void deleteSlot(UUID id);

    List<TaskData> getTasks();

    TaskData getTaskById(UUID id);

    void addTask(TaskData task);

    void removeTask(UUID id);

    void moveTask(int position, UUID taskId, UUID slotId);

    List<TaskTemplateData> getTaskTemplates();

    TaskTemplateData getTemplateById(UUID id);

    void addTaskTemplate(TaskTemplateData taskTemplate);

    void removeTaskTemplate(UUID id);
}
