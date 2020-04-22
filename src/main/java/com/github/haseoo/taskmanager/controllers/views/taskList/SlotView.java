package com.github.haseoo.taskmanager.controllers.views.taskList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Value;

import java.util.UUID;

@Value
public class SlotView {
    UUID id;
    SimpleStringProperty name;
    SimpleIntegerProperty position;

    public SlotView(String name, int position) {
        this.name = new SimpleStringProperty(name);
        id = null;
        this.position = new SimpleIntegerProperty(position);
    }
}
