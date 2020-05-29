package com.github.haseoo.taskmanager.oldcontrollers.views;

import com.github.haseoo.taskmanager.oldcontrollers.views.tasklist.TagView;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TagTableView {
    UUID id;
    String name;
    long taskCount;

    public static TagTableView from(TagView tagView, long count) {
        return new TagTableView(tagView.getId(), tagView.getName().getValue(), count);
    }
}
