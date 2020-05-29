package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.SlotData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER;

@RequiredArgsConstructor
public class SlotController {
    private final JFXServiceImpl jfxService;
    private final SlotData currentSlot;

    @Setter
    @Getter
    private Node slotNode;

    @FXML
    private Text slotLabel;
    @FXML
    private VBox slotVBox;
    @FXML
    private ScrollPane taskListScrollPane;

    @FXML
    private void initialize() {
        currentSlot.bindName(slotLabel.textProperty());
        jfxService.onSlotInitialization(currentSlot.getId(), slotVBox.getChildren());
    }

    @FXML
    private void onSlotEdit() throws IOException {
        var dialogController = prepareInputDialog();
        dialogController.getNewName().ifPresent(currentSlot::setName);
        dialogController.getNewPosition().ifPresent(position -> jfxService.moveSlot(currentSlot.getId(), position));
    }


    @FXML
    private void onSlotDelete() {
        deleteConfirmationDialog(
                String.format("Are you sure do delete slot %s", currentSlot.getName()),
                this::removeSlot
        );
    }


    @FXML
    private void onAddCard() throws IOException {
        jfxService.addNewTask(currentSlot.getId());
    }

    @FXML
    void showScrollBar() {
        taskListScrollPane.setVbarPolicy(AS_NEEDED);
    }

    @FXML
    void hideScrollBar() {
        taskListScrollPane.setVbarPolicy(NEVER);
    }

    private void removeSlot() {
        jfxService.deleteSlot(currentSlot.getId());
    }

    private SlotDialogController prepareInputDialog() throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/slotDialog.fxml"));
        var slotDialogController = new SlotDialogController(currentSlot, jfxService.getSlotCount());
        loader.setController(slotDialogController);
        Parent root = loader.load();
        var dialog = prepareDialog(root, String.format("Edit %s", currentSlot.getName()));
        dialog.setResizable(false);
        dialog.showAndWait();
        return slotDialogController;
    }
}
