package com.github.haseoo.taskmanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    private UUID id;
    private String name;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String description;
    private int position;

    private UUID slotId;
    private UUID tagId;

    private List<SubTask> subTasks;
}
