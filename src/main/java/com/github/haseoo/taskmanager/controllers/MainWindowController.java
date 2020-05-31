package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.utilities.URLs;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Constants.CALENDAR_WINDOW_TITLE;
import static com.github.haseoo.taskmanager.utilities.Constants.TAG_WINDOW_TITLE;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.TASK_LIST_TITLE_EDIT_DIALOG_HEADER;
import static com.github.haseoo.taskmanager.utilities.DialogStrings.TASK_LIST_TITLE_EDIT_DIALOG_TITLE;
import static com.github.haseoo.taskmanager.utilities.Utilities.prepareWindow;
import static com.github.haseoo.taskmanager.utilities.Utilities.textInputDialog;

@RequiredArgsConstructor
public class MainWindowController {
    private final JFXServiceImpl jfxService;
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

    }

    @FXML
    private void onOpen() {

    }

    @FXML
    private void onSave() throws IOException {
        jfxService.saveList();
    }

    @FXML
    private void onExport() {
        System.out.println("EXPORT!");
    }

    @FXML
    private void onCalendar() throws IOException {
        prepareWindow(FxmlFilePaths.CALENDAR_WINDOW,
                CALENDAR_WINDOW_TITLE,
                new CalendarWindowController(jfxService)
        );
    }

    @FXML
    private void onSearch() {

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
        jfxService.addSlot();
    }

    @FXML
    void onTitleEdit() {
        textInputDialog(taskListTitle.getText(),
                TASK_LIST_TITLE_EDIT_DIALOG_TITLE,
                TASK_LIST_TITLE_EDIT_DIALOG_HEADER,
                name -> jfxService.getCurrentTaskList().setName(name));
    }
}
