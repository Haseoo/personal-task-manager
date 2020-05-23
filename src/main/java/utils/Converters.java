package utils;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static utils.Constants.COLOR_COMPONENT_MAX_VALUE;

@AllArgsConstructor(access = PRIVATE)
public final class Converters {
    public static int JFXColorRangeToRGBRange(double JFXColorComponent) {
        return (int) (JFXColorComponent * COLOR_COMPONENT_MAX_VALUE);
    }
}
