package com.github.haseoo.taskmanager.controllers.views.tasklist;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PACKAGE;

@Value
@AllArgsConstructor(access = PACKAGE)
public class TagView {
    UUID id;
    SimpleStringProperty name;

    public static TagView form(String name) {
        return new TagView(randomUUID(), new SimpleStringProperty(name));
    }
}
