package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.TagColorData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagColor {
    private int red;
    private int green;
    private int blue;

    public static TagColor from(TagColorData tagColorData) {
        return new TagColor(tagColorData.getRed(), tagColorData.getGreen(), tagColorData.getBlue());
    }
}
