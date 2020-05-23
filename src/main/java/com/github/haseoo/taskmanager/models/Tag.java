package com.github.haseoo.taskmanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private UUID id;
    private String name;
    private TagColor color;
}
