package com.github.haseoo.taskmanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagColor {
    private byte red;
    private byte blue;
    private byte green;
}
