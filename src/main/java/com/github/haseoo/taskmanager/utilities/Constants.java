package com.github.haseoo.taskmanager.utilities;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class Constants {
    public static final int COLOR_COMPONENT_MAX_VALUE = 255;
    public static final String APPLICATION_NAME = "Personal task manager";
    public static final String TAG_WINDOW_TITLE = "Tags";
    public static final String DEFAULT_COLOR = "#4BA9EF";
    public static final String CELL_BG_COLOR_STYLE_FORMAT = "-fx-background-color: %s";
    public static final String TRANSPARENT_CELL_BG_COLOR_STYLE = "-fx-background-color: transparent";
    public static final String CARD_STYLE = "-fx-background-color: rgba(255, 255, 255, 1);-fx-background-radius: 5;-fx-border-style: solid;-fx-border-width: 1px;-fx-border-color: gray;-fx-background-radius: 10;-fx-border-radius: 10;-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 5, 0, 0, 0);";
    public static final String HOVER_CARD_STYLE_FORMAT = CARD_STYLE + "-fx-background-color: linear-gradient(from 0%% 30%% to 100%% 100%%, white, rgb(%s, %s, %s));-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);";
    public static final String CARD_TITLE_STYLE_FORMAT = "-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: rgb(%s, %s, %s);";
}
