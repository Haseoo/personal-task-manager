package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.utilities.URLs;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

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
    private void onCalendar() {

    }

    @FXML
    private void onSearch() {

    }

    @FXML
    private void onTag() {

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
                "Edit task list name",
                "Enter new name",
                name -> jfxService.getCurrentTaskList().setName(name));
    }
}
