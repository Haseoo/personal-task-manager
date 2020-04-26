package com.github.haseoo.taskmanager.controllers.views.tasklist;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
public class SlotView {
    UUID id;
    SimpleStringProperty name;
    SimpleIntegerProperty position;
    List<TaskView> tasks = new ArrayList<>();

    public SlotView(String name, int position) {
        this.name = new SimpleStringProperty(name);
        id = UUID.randomUUID();
        this.position = new SimpleIntegerProperty(position);
    }
}
