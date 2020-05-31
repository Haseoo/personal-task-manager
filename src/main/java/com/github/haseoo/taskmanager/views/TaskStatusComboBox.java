package com.github.haseoo.taskmanager.views;

import java.util.Arrays;

public enum TaskStatusComboBox {
    ALL("All tasks"),
    DELAYED("Delayed"),
    URGENT("Urgent");

    private String string;

    @Override
    public String toString() {
        return string;
    }

    TaskStatusComboBox(String status) {
        this.string = status;
    }

    public static TaskStatusComboBox from(String status) {
        return Arrays.stream(values()).filter(e -> e.string.equals(status)).findAny().orElseThrow(InstantiationError::new);
    }
}
