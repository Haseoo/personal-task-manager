package com.github.haseoo.taskmanager.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.github.haseoo.taskmanager.utilities.DefaultValues.DEFAULT_TASK_LIST_NAME;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class TaskListData {
    private final StringProperty name;
    @Getter
    private final List<TagData> tags;
    @Getter
    private final List<SlotData> slots;
    @Getter
    private final List<TaskTemplateData> taskTemplates;

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void bindName(StringProperty name) {
        name.bindBidirectional(this.name);
    }

    public static TaskListData newInstance() {
        return new TaskListData(new SimpleStringProperty(DEFAULT_TASK_LIST_NAME),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }
}
