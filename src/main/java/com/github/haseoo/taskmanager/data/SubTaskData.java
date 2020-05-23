package com.github.haseoo.taskmanager.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class SubTaskData {
    private final StringProperty name;
    private final BooleanProperty complete;

    public static SubTaskData newInstance(String name, boolean complete) {
        return new SubTaskData(new SimpleStringProperty(name), new SimpleBooleanProperty(complete));
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

    public boolean isComplete() {
        return complete.get();
    }

    public void setComplete(boolean complete) {
        this.complete.set(complete);
    }

    public void bindComplete(BooleanProperty complete) {
        this.complete.bindBidirectional(complete);
    }
}
