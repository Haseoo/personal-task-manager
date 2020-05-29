package com.github.haseoo.taskmanager.data;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.github.haseoo.taskmanager.utilities.Converters.JFXColorRangeToRGBRange;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TagColorData {
    int red;
    int blue;
    int green;

    public static TagColorData newInstance(Color color) {
        return new TagColorData(JFXColorRangeToRGBRange(color.getRed()),
                JFXColorRangeToRGBRange(color.getBlue()),
                JFXColorRangeToRGBRange(color.getGreen()));
    }
}
