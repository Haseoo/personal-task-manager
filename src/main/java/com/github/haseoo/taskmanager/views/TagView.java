package com.github.haseoo.taskmanager.views;

import com.github.haseoo.taskmanager.data.TagData;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.github.haseoo.taskmanager.utilities.Converters.tagColorToFfxColor;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TagView {
    UUID id;
    String name;
    long taskCount;
    Color color;

    public static TagView from(TagData tagData, long taskCount) {
        return new TagView(tagData.getId(),
                tagData.getName(),
                taskCount,
                tagColorToFfxColor(tagData.getTagColor()));
    }

    public static TagView from(TagData tagData) {
        return new TagView(tagData.getId(),
                tagData.getName(),
                0,
                tagColorToFfxColor(tagData.getTagColor()));
    }

    public String getColorValue() {
        return color.toString();
    }

    @Override
    public String toString() {
        return name;
    }
}
