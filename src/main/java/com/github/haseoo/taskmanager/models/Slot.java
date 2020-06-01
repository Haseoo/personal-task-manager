package com.github.haseoo.taskmanager.models;

import com.github.haseoo.taskmanager.data.SlotData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private UUID id;
    private String name;
    private int position;

    public static Slot form(SlotData slotData) {
        return new Slot(slotData.getId(),
                slotData.getName(),
                slotData.getPosition());
    }
}
