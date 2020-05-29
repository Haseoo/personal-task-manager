package com.github.haseoo.taskmanager.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static utils.DefaultValues.DEFAULT_TASK_LIST_NAME;

@AllArgsConstructor(access = PRIVATE)
public final class TaskListData {
    @Getter
    private final UUID id;
    private final StringProperty name;
    @Getter
    private final List<TagData> tags;
    @Getter
    private final List<SlotData> slots;

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void bindName(StringProperty name) {
        this.name.bindBidirectional(name);
    }

    public static TaskListData defaultNewInstance() {
        return new TaskListData(UUID.randomUUID(),
                new SimpleStringProperty(DEFAULT_TASK_LIST_NAME),
                new ArrayList<>(),
                new ArrayList<>());
    }
}
