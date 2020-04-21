package com.github.haseoo.taskmanager.controllers.views;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Value
public class OpenWindowTableRecord {
    public OpenWindowTableRecord(String name, LocalDateTime dateTime) {
        this.tasksListName = name;
        this.lastOpened = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

    String tasksListName;
    String lastOpened;
}
