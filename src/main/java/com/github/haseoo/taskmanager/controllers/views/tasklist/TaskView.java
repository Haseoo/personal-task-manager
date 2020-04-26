package com.github.haseoo.taskmanager.controllers.views.tasklist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class TaskView {
    UUID id;
    SimpleStringProperty name;
    SimpleStringProperty dateFrom;
    SimpleStringProperty dateTo;
    SimpleStringProperty completeness;
    SimpleStringProperty tagName;
    SimpleStringProperty description;
    SimpleIntegerProperty position;
    List<SubTaskView> subTasks;
}
