package com.github.haseoo.taskmanager.utilities;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DialogStrings {
    public static final String TASK_LIST_TITLE_EDIT_DIALOG_TITLE = "Edit task list name";
    public static final String TASK_LIST_TITLE_EDIT_DIALOG_HEADER = "Enter new name";
    public static final String SLOT_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT = "Are you sure do delete slot %s";
    public static final String SLOT_EDIT_WINDOW_TITLE_FORMAT = "Edit %s";
    public static final String TASK_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT = "Are you sure do delete task %s";
    public static final String TASK_MOVE_DIALOG_TITLE_FORMAT = "Move task %s";
}
