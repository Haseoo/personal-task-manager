package com.github.haseoo.taskmanager.controllers.views.taskList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TaskListView {

    public static TaskListView DEFAULT_VALUE = prepareDefault();

    StringProperty title;
    List<TagView> tags;
    List<SlotView> slots;


    private static TaskListView prepareDefault() {
        List<TagView> tags = new ArrayList<>();
        List<SlotView> slots = new ArrayList<>();
        tags.add(new TagView(UUID.fromString("00000000-0000-0000-0000-000000000001"), new SimpleStringProperty("Tag1")));
        tags.add(new TagView(UUID.fromString("00000000-0000-0000-0000-000000000002"), new SimpleStringProperty("Tag2")));

        slots.add(new SlotView("Slot1", 0));
        slots.add(new SlotView("Slot2", 1));

        return new TaskListView(new SimpleStringProperty("Untitled"),
                tags,
                slots);
    }
}
