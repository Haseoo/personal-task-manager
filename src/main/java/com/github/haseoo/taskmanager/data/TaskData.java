package com.github.haseoo.taskmanager.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static lombok.AccessLevel.PRIVATE;

@Builder(access = PRIVATE)
public final class TaskData {
    private final UUID id;
    private final StringProperty name;
    private final ObjectProperty<LocalDate> dateFrom;
    private final StringProperty dateFromString;
    private final ObjectProperty<LocalDate> dateTo;
    private final StringProperty dateToString;
    private final StringProperty description;
    @Getter
    @Setter
    private SlotData slot;

    private final BiFunction<UUID, UUID, Integer> getPosition;

    private final AtomicReference<TagData> tag;


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void bindName(StringProperty name) {
        this.name.bindBidirectional(name);
    }

    public LocalDate getDateFrom() {
        return dateFrom.get();
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom.set(dateFrom);
    }

    public void bindDateFrom(StringProperty dateFromString) {
        this.dateFromString.bindBidirectional(dateFromString);
    }

    public LocalDate getDateTo() {
        return dateTo.get();
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo.set(dateTo);
    }

    public void bindDateTo(StringProperty dateToString) {
        this.dateToString.bindBidirectional(dateToString);
    }

    public String getDescription() {
        return description.getValueSafe();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public void bindDescription(StringProperty description) {
        this.description.bindBidirectional(description);
    }

    public TagData getTag() {
        return tag.get();
    }

    public void setTag(TagData tag) {
        this.tag.set(tag);
    }
}
