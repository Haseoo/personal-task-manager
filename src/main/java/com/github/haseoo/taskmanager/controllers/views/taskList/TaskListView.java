package com.github.haseoo.taskmanager.controllers.views.taskList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class TaskListView {
    StringProperty title = new SimpleStringProperty("Untitled");
}
