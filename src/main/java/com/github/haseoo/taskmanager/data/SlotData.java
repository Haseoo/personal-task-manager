package com.github.haseoo.taskmanager.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;
import static utils.DefaultValues.DEFAULT_SLOT_NAME;

@RequiredArgsConstructor(access = PRIVATE)
public final class SlotData {
    private final UUID id;
    private final StringProperty name;
    @Getter
    private final List<TaskData> tasks;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    @Setter
    private Function<UUID, Integer> positionSupplier;

    public void bindName(StringProperty name) {
        name.bindBidirectional(this.name);
    }

    public int getPosition() {
        return positionSupplier.apply(id);
    }

    public static SlotData defaultInstance() {
        return new SlotData(UUID.randomUUID(),
                new SimpleStringProperty(DEFAULT_SLOT_NAME),
                new ArrayList<>()
        );
    }
}
