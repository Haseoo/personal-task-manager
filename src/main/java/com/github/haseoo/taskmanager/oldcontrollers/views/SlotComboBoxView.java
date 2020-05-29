package com.github.haseoo.taskmanager.oldcontrollers.views;

import com.github.haseoo.taskmanager.oldcontrollers.views.tasklist.SlotView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = PRIVATE)
public class SlotComboBoxView {

    private final UUID id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    public static SlotComboBoxView from(SlotView slot) {
        return new SlotComboBoxView(slot.getId(), slot.getName().getValueSafe());
    }

}
