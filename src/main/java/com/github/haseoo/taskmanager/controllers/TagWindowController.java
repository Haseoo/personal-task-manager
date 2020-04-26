package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.TagTableView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TagView;
import com.github.haseoo.taskmanager.controllers.views.tasklist.TaskListView;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

import static com.github.haseoo.taskmanager.utilities.Utilities.deleteConfirmationDialog;
import static com.github.haseoo.taskmanager.utilities.Utilities.textInputDialog;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class TagWindowController {
    private final AtomicReference<TaskListView> taskListView;

    @FXML
    TableView<TagTableView> tagTable;

    @FXML
    void initialize() {
        tagTable.getItems().addAll(taskListView.get().getTags().stream().map(TagTableView::from).collect(toList()));
    }

    @FXML
    void onAdd() {
        textInputDialog("name",
                "Add new tag",
                "Enter name",
                this::addNewTag);
    }

    @FXML
    void onRemove() {
        var selected = tagTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteConfirmationDialog(String.format("Are you sure to delete tag: %s?", selected.getName()),
                    () -> tagTable.getItems().remove(selected));
        }

    }

    @FXML
    void onEdit() {
        var selected = tagTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            textInputDialog(selected.getName(),
                    "Rename tag",
                    "Enter new name",
                    name -> {
                        editTag(selected, name);
                    });
        }
    }

    private void editTag(TagTableView selected, String name) {
        var toEdit = taskListView.get().getTags().stream()
                .filter(tag -> tag.getId().equals(selected.getId()))
                .findAny()
                .orElseThrow(AssertionError::new);
        toEdit.getName().setValue(name);
        tagTable.getItems()
                .set(tagTable.getSelectionModel().getSelectedIndex(),
                        TagTableView.from(toEdit));
    }

    void addNewTag(String tagName) {
        var tag = TagView.form(tagName);
        taskListView.get().getTags().add(tag);
        tagTable.getItems().add(TagTableView.from(tag));
    }
}
