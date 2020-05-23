package com.github.haseoo.taskmanager.data;

import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class TaskListData {
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
}
