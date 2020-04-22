package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.taskList.SlotView;
import com.github.haseoo.taskmanager.controllers.views.taskList.TaskListView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

import static com.github.haseoo.taskmanager.utilities.Utilities.*;

public class SlotController {
    private final AtomicReference<TaskListView> taskListView;
    private final List<Node> slotPanes;
    private final List<SlotController> otherSlots;

    @Getter
    private GridPane currentPane;
    @Getter
    private SlotView currentSlot;

    @FXML
    private Label slotLabel;

    public SlotController(AtomicReference<TaskListView> taskListView,
                          List<Node> slotPanes,
                          List<SlotController> otherSlots,
                          SlotView currentSlot) {
        this.taskListView = taskListView;
        this.currentSlot = currentSlot;
        this.slotPanes = slotPanes;
        this.otherSlots = otherSlots;
    }

    public SlotController(AtomicReference<TaskListView> taskListView,
                          List<Node> slotPanes,
                          List<SlotController> otherSlots) {
        this.taskListView = taskListView;
        this.slotPanes = slotPanes;
        this.otherSlots = otherSlots;
        currentSlot = new SlotView("newSlot", slotPanes.size());
        taskListView.get().getSlots().add(currentSlot);
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
        slotDialogController.getNewPosition().ifPresent(this::moveSlot);
    }

    @FXML
    void onSlotDelete() {
        deleteConfirmationDialog(
                String.format("Are you sure do delete slot %s", currentSlot.getName().getValueSafe()),
                this::removeSlot
        );
    }

    @FXML
    void onAddCard() {

    }

    private SlotDialogController prepareInputDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getResourceURL("FXML/slotDialog.fxml"));
        var slotDialogController = new SlotDialogController(currentSlot, slotPanes.size());
        loader.setController(slotDialogController);
        Parent root = loader.load();
        var dialog = prepareDialog(root, String.format("Edit %s", currentSlot.getName().getValueSafe()));
        dialog.setResizable(false);
        dialog.showAndWait();
        return slotDialogController;
    }

    private void moveSlot(int newPosition) {
        currentSlot.getPosition().setValue(newPosition);
        int newIndex = currentSlot.getPosition().getValue();
        slotPanes.remove(currentPane);
        slotPanes.add(newIndex, currentPane);
        updateSlotPositions();
    }

    private void removeSlot() {
        slotPanes.remove(currentPane);
        slotPanes.remove(currentPane);
        taskListView.get().getSlots().remove(currentSlot);
        updateSlotPositions();
    }

    private void updateSlotPositions() {
        for (var slot : otherSlots) {
            slot.updatePosition(slotPanes.indexOf(slot.getCurrentPane()));
        }
    }

    @FXML
    void foo (MouseEvent e) {
        System.out.println("SOURCE: " + e.getSource().hashCode());
        var n = (Node) e.getSource();
        System.out.println("SUROCEE:" + n.getParent().hashCode());
    }

    @FXML
    void bar (MouseEvent e) {
        System.out.println("SOURCE2: " + e.getSource().hashCode());
    }

}
