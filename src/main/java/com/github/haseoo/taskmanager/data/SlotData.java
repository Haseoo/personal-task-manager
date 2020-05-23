package com.github.haseoo.taskmanager.data;

import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class SlotData {
    private final UUID id;
    private final StringProperty name;
    @Getter
    private final List<TaskData> tasks;

    public UUID getId() {
        return id;
    }

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
