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
    public static final String ADD_TAG_DIALOG_TITLE = "Add tag";
    public static final String EDIT_TAG_DIALOG_TITLE_FORMAT = "Edit tag %s";
    public static final String TAG_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT = "Are you sure do delete tag %s";
    public static final String INVALID_TASK_DATE_PROMPT = "Task's date from is after task's date to.";
    public static final String INVALID_TASK_NAME_PROMPT = "You've entered empty task name";
    public static final String SAVE_FILE_TITLE = "Save task list as";
    public static final String SAVE_FILE_DESCRIPTION = "Task list file";
    public static final String SAVE_FILE_EXTENSION = "*.tsk";
}
