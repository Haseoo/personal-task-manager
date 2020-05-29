package com.github.haseoo.taskmanager.views;

import com.github.haseoo.taskmanager.data.SlotData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = PRIVATE)
public class SlotView {

    private final UUID id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    public static SlotView from(SlotData slot) {
        return new SlotView(slot.getId(), slot.getName());
    }

}
