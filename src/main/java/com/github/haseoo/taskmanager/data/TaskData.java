package com.github.haseoo.taskmanager.data;

import com.github.haseoo.taskmanager.models.Task;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.github.haseoo.taskmanager.utilities.Converters.LOCAL_DATE_STRING_CONVERTER;
import static com.github.haseoo.taskmanager.utilities.DefaultValues.DEFAULT_TASK_NAME;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public final class TaskData {
    private final UUID id;
    private final StringProperty name;
    private final ObjectProperty<LocalDate> dateFrom;
    private final ObjectProperty<LocalDate> dateTo;
    private final StringProperty description;
    @Getter
    @Setter
    private SlotData slot;

    @Setter
    private Function<TaskData, Integer> positionSupplier;

    private final ObjectProperty<TagData> tag;

    @Getter
    private final List<SubTaskData> subTasks = new ArrayList<>();
    @Getter
    private final List<String> keyWords = new ArrayList<>();


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
        name.bindBidirectional(this.name);
    }

    public LocalDate getDateFrom() {
        return dateFrom.get();
    }


    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom.set(dateFrom);
    }

    public void bindDateFrom(StringProperty dateFromString) {
        dateFromString.bindBidirectional(dateFrom, LOCAL_DATE_STRING_CONVERTER);
    }

    public LocalDate getDateTo() {
        return dateTo.get();
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo.set(dateTo);
    }

    public void bindDateTo(StringProperty dateToString) {
        dateToString.bindBidirectional(dateTo, LOCAL_DATE_STRING_CONVERTER);
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

    public void addTagListener(ChangeListener<? super TagData> listener) {
        tag.addListener(listener);
    }

    public void removeTagListener(ChangeListener<? super TagData> listener) {
        tag.removeListener(listener);
    }

    public int getPosition() {
        return positionSupplier.apply(this);
    }

    public double getCompleteness() {
        var taskCount = getSubTasks().size();
        var completeTaskCount = getSubTasks().stream().filter(SubTaskData::isComplete).count();
        return 100.0 * (double) completeTaskCount / (double) taskCount;
    }

    public static TaskData defaultInstance(SlotData slot) {
        var data = new TaskData(UUID.randomUUID(),
                new SimpleStringProperty(DEFAULT_TASK_NAME),
                new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>(),
                new SimpleStringProperty(),
                new SimpleObjectProperty<>());
        data.setSlot(slot);
        return data;
    }

    public static TaskData from(Task task) {
        var taskData = new TaskData(task.getId(),
                new SimpleStringProperty(task.getName()),
                new SimpleObjectProperty<>(task.getDateFrom()),
                new SimpleObjectProperty<>(task.getDateTo()),
                new SimpleStringProperty(task.getDescription()),
                new SimpleObjectProperty<>());
        task.getSubTasks().stream().map(SubTaskData::from).forEach(taskData.subTasks::add);
        taskData.getKeyWords().addAll(task.getKeyWords());
        return taskData;
    }
}
