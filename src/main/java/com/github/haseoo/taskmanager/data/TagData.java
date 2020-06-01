package com.github.haseoo.taskmanager.data;

import com.github.haseoo.taskmanager.models.Tag;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public final class TagData {
    @Getter
    private final UUID id;
    private final StringProperty name;
    private final ObjectProperty<TagColorData> color;

    public String getName() {
        return name.getValueSafe();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void bindName(StringProperty name) {
        name.bindBidirectional(this.name);
    }

    public void unbindName(StringProperty name) {
        this.name.unbindBidirectional(name);
    }

    public TagColorData getTagColor() {
        return color.getValue();
    }

    public void setTagColor(TagColorData color) {
        this.color.setValue(color);
    }

    public void addColorListener(ChangeListener<? super TagColorData> listener) {
        color.addListener(listener);
    }

    public void removeColorListener(ChangeListener<? super TagColorData> listener) {
        color.removeListener(listener);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public static TagData newInstance(String tagName, TagColorData color) {
        return new TagData(UUID.randomUUID(), new SimpleStringProperty(tagName), new SimpleObjectProperty<>(color));
    }

    public static TagData from(Tag tag) {
        return new TagData(tag.getId(),
                new SimpleStringProperty(tag.getName()),
                new SimpleObjectProperty<>(TagColorData.from(tag.getColor())));
    }
}
