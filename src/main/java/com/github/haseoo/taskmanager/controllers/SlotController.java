package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.taskList.SlotView;
import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;

public class SlotController {
    private final AtomicReference<TaskListView> taskListView;
    private final Consumer<SlotController> onSlotRemove;
    private final IntSupplier getSlotMaxPosition;
    private final Consumer<SlotController> onSlotMove;

    @Getter
    private GridPane currentPane;
    @Getter
    private SlotView currentSlot;

    @FXML
    private Label slotLabel;

    public SlotController(AtomicReference<TaskListView> taskListView,
                          Consumer<SlotController> onSlotRemove,
                          Consumer<SlotController> onSlotMove,
                          IntSupplier getSlotMaxPosition,
                          SlotView currentSlot) {
        this.taskListView = taskListView;
        this.onSlotRemove = onSlotRemove;
        this.currentSlot = currentSlot;
        this.onSlotMove = onSlotMove;
        this.getSlotMaxPosition = getSlotMaxPosition;
    }

    public SlotController(AtomicReference<TaskListView> taskListView,
                          Consumer<SlotController> onSlotRemove,
                          Consumer<SlotController> onSlotMove,
                          IntSupplier getSlotMaxPosition,
                          int position) {
        this.taskListView = taskListView;
        this.onSlotRemove = onSlotRemove;
        currentSlot = new SlotView("newSlot", position);
        taskListView.get().getSlots().add(currentSlot);
        this.onSlotMove = onSlotMove;
        this.getSlotMaxPosition = getSlotMaxPosition;
    }

    public void setPane(GridPane pane) {
        if (currentPane == null) {
            currentPane = pane;
        }
    }

    public void updatePosition(int newPosition) {
        currentSlot.getPosition().setValue(newPosition);
    }

    @FXML
    void initialize() {
        slotLabel.textProperty().bindBidirectional(currentSlot.getName());
    }

    @FXML
    void onSlotEdit() throws IOException {
        var slotDialogController = prepareInputDialog();
        slotDialogController.getNewName().ifPresent(currentSlot.getName()::setValue);
        slotDialogController.getNewPosition().ifPresent(newPosition -> {
            currentSlot.getPosition().setValue(newPosition);
            onSlotMove.accept(this);
        });
    }

    @FXML
    void onSlotDelete() {
        deleteConfirmationDialog(
                String.format("Are you sure do delete slot %s", currentSlot.getName().getValueSafe()),
                () -> onSlotRemove.accept(this)
        );
    }

    @FXML
    void onAddCard() {

    }

    private SlotDialogController prepareInputDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/slotDialog.fxml"));
        var slotDialogController = new SlotDialogController(currentSlot, getSlotMaxPosition.getAsInt());
        loader.setController(slotDialogController);
        Parent root = loader.load();
        var dialog = prepareDialog(root, String.format("Edit %s", currentSlot.getName().getValueSafe()));
        dialog.setResizable(false);
        dialog.showAndWait();
        return slotDialogController;
    }

}
