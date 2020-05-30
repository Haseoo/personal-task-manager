package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.TagData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private UUID id;
    private String name;
    private TagColor color;

    public static Tag from(TagData tagData) {
        return new Tag(tagData.getId(), tagData.getName(), TagColor.from(tagData.getTagColor()));
    }
}
