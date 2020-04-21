package com.github.haseoo.taskmanager.controllers.views;

import com.github.haseoo.taskmanager.controllers.views.taskList.TagView;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TagTableView {
    UUID id;
    String name;
    int taskCount;

    public static TagTableView from(TagView tagView) {
        return new TagTableView(tagView.getId(), tagView.getName().getValue(), 0);
    }
}
