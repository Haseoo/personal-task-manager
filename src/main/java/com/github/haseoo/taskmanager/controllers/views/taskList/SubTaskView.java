package com.github.haseoo.taskmanager.controllers.views.taskList;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Value;

@Value
public class SubTaskView {
    SimpleStringProperty name;
    SimpleBooleanProperty completed;
}
