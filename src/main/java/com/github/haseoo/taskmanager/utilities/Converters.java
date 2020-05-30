package com.github.haseoo.taskmanager.utilities;

import com.github.haseoo.taskmanager.data.TagColorData;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static com.github.haseoo.taskmanager.utilities.Constants.COLOR_COMPONENT_MAX_VALUE;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class Converters {
    public static int JFXColorRangeToRGBRange(double JFXColorComponent) {
        return (int) (JFXColorComponent * COLOR_COMPONENT_MAX_VALUE);
    }

    public static double RGBRangeTOJFXColorRange(int rgbRange) {
        return (double) rgbRange / (double) COLOR_COMPONENT_MAX_VALUE;
    }

    public static Color tagColorToFfxColor(TagColorData tagColor) {
        return Color.color(RGBRangeTOJFXColorRange(tagColor.getRed()),
                RGBRangeTOJFXColorRange(tagColor.getGreen()),
                RGBRangeTOJFXColorRange(tagColor.getBlue()));
    }

    public static final StringConverter<LocalDate> LOCAL_DATE_STRING_CONVERTER = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate localDate) {
            if (localDate != null) {
                return localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }
            return "";
        }

        @Override
        public LocalDate fromString(String s) {
            throw new UnsupportedOperationException();
        }
    };

    public static String colorStringNormalize(String hexColor) {
        return hexColor.replace("0x", "#");
    }
}
