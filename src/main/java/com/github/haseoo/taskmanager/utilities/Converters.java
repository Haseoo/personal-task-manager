package com.github.haseoo.taskmanager.utilities;

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
}
