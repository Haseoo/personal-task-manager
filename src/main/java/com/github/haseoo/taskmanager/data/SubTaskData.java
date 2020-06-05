package com.github.haseoo.taskmanager.data;

import com.github.haseoo.taskmanager.models.SubTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;

import static com.github.haseoo.taskmanager.utilities.DefaultValues.DEFAULT_SUBSTASK_NAME;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class SubTaskData {
    private final StringProperty name;
    private final BooleanProperty complete;

    public static SubTaskData newInstance() {
        return new SubTaskData(new SimpleStringProperty(DEFAULT_SUBSTASK_NAME), new SimpleBooleanProperty());
    }

    public static SubTaskData newInstance(String name) {
        return new SubTaskData(new SimpleStringProperty(name), new SimpleBooleanProperty());
    }

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void bindName(StringProperty name) {
        name.bindBidirectional(this.name);
    }

    public boolean isComplete() {
        return complete.get();
    }

    public void setComplete(boolean complete) {
        this.complete.set(complete);
    }

    public void bindComplete(BooleanProperty complete) {
        complete.bindBidirectional(this.complete);
    }

    public static SubTaskData from(SubTask subTask) {
        return new SubTaskData(new SimpleStringProperty(subTask.getName()),
                new SimpleBooleanProperty(subTask.isComplete()));
    }
}
