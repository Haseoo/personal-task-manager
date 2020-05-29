package com.github.haseoo.taskmanager.oldcontrollers.views.tasklist;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Value;

@Value
public class SubTaskView {
    SimpleStringProperty name;
    SimpleBooleanProperty completed;
}
