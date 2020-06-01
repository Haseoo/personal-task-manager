package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.services.adapters.FileServiceImpl;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.utilities.URLs;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Constants.*;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.*;
import static com.github.haseoo.taskmanager.utilities.Utilities.*;

@RequiredArgsConstructor
public class MainWindowController {
    private final JFXServiceImpl jfxService;
    private final FileServiceImpl fileService;
    @FXML
    private Label taskListTitle;
    @FXML
    private HBox mainHBox;

    @FXML
    void initialize() {
        jfxService.setMainWindowController(this); //TODO DO I NEED THIS?
        jfxService.getCurrentTaskList().bindName(taskListTitle.textProperty());
        jfxService.setSlotHBox(mainHBox);
    }

    @FXML
    private void onNew() {
        confirmationDialog(NEW_LIST_CONFIRMATION, LIST_OVERRIDE_TEXT, () -> {
            jfxService.newList();
            jfxService.getCurrentTaskList().bindName(taskListTitle.textProperty());
        });
    }

    @FXML
    private void onOpen() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle(OPEN_FILE_TITLE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(SAVE_FILE_DESCRIPTION, SAVE_FILE_EXTENSION));
        var file = fileChooser.showOpenDialog((Stage) taskListTitle.getScene().getWindow());
        if (file != null) {
            try {
                jfxService.loadFromModel(fileService.openTaskList(file));
                jfxService.getCurrentTaskList().bindName(taskListTitle.textProperty());
                fileService.setSavedOutput(file);
            } catch (Exception | AssertionError e) {
                e.printStackTrace();
                showOpeningError();
            }
        }
    }

    @FXML
    private void onSave() {
        if (fileService.isOutputSaved()) {
            try {
                fileService.saveFile(jfxService.getModel());
                showSavedInfo();
            } catch (IOException e) {
                showSavingError();
                e.printStackTrace();
            }
        } else {
            onExport();
        }
    }

    @FXML
    private void onExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(SAVE_FILE_TITLE);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(SAVE_FILE_DESCRIPTION, SAVE_FILE_EXTENSION));
        fileChooser.setInitialFileName(taskListTitle.getText());
        var file = fileChooser.showSaveDialog((Stage) taskListTitle.getScene().getWindow());
        if (file != null) {
            try {
                fileService.saveFileAs(file, jfxService.getModel());
                showSavedInfo();
            } catch (IOException e) {
                showSavingError();
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onCalendar() throws IOException {
        prepareWindow(FxmlFilePaths.CALENDAR_WINDOW,
                CALENDAR_WINDOW_TITLE,
                new CalendarWindowController(jfxService)
        );
    }

    @FXML
    private void onSearch() throws IOException {
        prepareWindow(FxmlFilePaths.SEARCH_WINDOW,
                SEARCH_WINDOW_TITLE,
                new SearchWindowController(jfxService));
    }

    @FXML
    private void onTag() throws IOException {
        prepareWindow(FxmlFilePaths.TAG_WINDOW,
                TAG_WINDOW_TITLE,
                new TagWindowController(jfxService));

    }

    @FXML
    private void onInfo() {
        jfxService.openUrl(URLs.PDF_URL);
    }

    @FXML
    private void onAddSlot() throws IOException {
        jfxService.addNewSlot();
    }

    @FXML
    void onTitleEdit() {
        textInputDialog(taskListTitle.getText(),
                TASK_LIST_TITLE_EDIT_DIALOG_TITLE,
                TASK_LIST_TITLE_EDIT_DIALOG_HEADER,
                name -> jfxService.getCurrentTaskList().setName(name));
    }
}
