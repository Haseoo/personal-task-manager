package com.github.haseoo.taskmanager.utilities;

import com.github.haseoo.taskmanager.data.TagColorData;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class DefaultValues {
    public static final String DEFAULT_TASK_LIST_NAME = "Untitled";
    public static final String DEFAULT_SLOT_NAME = "New slot";
    public static final String DEFAULT_TASK_NAME = "New task";
    public static final String DEFAULT_SUBSTASK_NAME = "New subtask";
    public static final TagColorData DEFAULT_TAG_COLOR = TagColorData.newInstance(Color.valueOf("#4BA9EF"));
}
