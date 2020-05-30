package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.views.TagView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Constants.CELL_BG_COLOR_STYLE_FORMAT;
import static com.github.haseoo.taskmanager.utilities.Constants.TRANSPARENT_CELL_BG_COLOR_STYLE;
import static com.github.haseoo.taskmanager.utilities.Converters.colorStringNormalize;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.*;
import static com.github.haseoo.taskmanager.utilities.Utilities.*;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class TagWindowController {
    private final JFXServiceImpl jfxService;

    @FXML
    private TableView<TagView> tagTable;

    @FXML
    @SuppressWarnings("unchecked")
    private void initialize() {
        TableColumn<TagView, String> col = (TableColumn<TagView, String>) tagTable.getColumns().get(1);
        col.setCellFactory(tagViewTableColumn -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                if (!empty) {
                    setStyle(String.format(CELL_BG_COLOR_STYLE_FORMAT, colorStringNormalize(item)));

                } else {
                    setStyle(TRANSPARENT_CELL_BG_COLOR_STYLE);
                }
            }
        });
        updateList();
    }

    @FXML
    private void onAdd() throws IOException {
        var controller = new TagDialogController();
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.TAG_DIALOG));
        loader.setController(controller);
        prepareDialog(loader.load(),
                ADD_TAG_DIALOG_TITLE).showAndWait();
        controller.getResponse().ifPresent(response -> {
            jfxService.addTag(response.getName(), response.getColor());
            updateList();
        });
    }

    @FXML
    private void onEdit() throws IOException {
        var selected = tagTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            var controller = new TagDialogController(selected);
            var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.TAG_DIALOG));
            loader.setController(controller);
            prepareDialog(loader.load(),
                    String.format(EDIT_TAG_DIALOG_TITLE_FORMAT, selected.getName())).showAndWait();
            controller.getResponse().ifPresent(response -> {
                jfxService.updateTag(selected.getId(),
                        response.getName(),
                        response.getColor());
                updateList();
            });
        }
    }

    @FXML
    private void onRemove() {
        var selected = tagTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteConfirmationDialog(String.format(TAG_DELETE_CONFIRMATION_DIALOG_PROMPT_FORMAT, selected.getName()),
                    () -> {
                        jfxService.removeTag(selected.getId());
                        updateList();
                    });
        }
    }

    private void updateList() {
        var tagList = jfxService.getTags();
        var tagTableList = tagTable.getItems();
        tagTableList.clear();
        tagTableList.addAll(tagList
                .stream()
                .map(tagData -> TagView.from(tagData,
                        jfxService.getTagTaskCount(tagData.getId())))
                .collect(toList()));
    }
}
