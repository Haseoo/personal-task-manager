package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TaskData;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.views.SlotView;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
public class MoveTaskDialogController {
    private final JFXServiceImpl jfxService;
    private final TaskData task;

    private Response response;

    public Optional<Response> getResponse() {
        return Optional.ofNullable(response);
    }

    @FXML
    private Label fromSlot;
    @FXML
    private ComboBox<SlotView> toSlot;
    @FXML
    private Spinner<Integer> position;

    @FXML
    void initialize() {
        task.getSlot().bindName(fromSlot.textProperty());
        toSlot.getItems().addAll(jfxService.getSlots().stream().map(SlotView::from).collect(toList()));
        toSlot.setValue(SlotView.from(task.getSlot()));
        toSlot.valueProperty().addListener(this::updatePositionSpinner);
    }

    @FXML
    void onCancel() {
        closeWindow();
    }

    @FXML
    void onSave() {
        var userSlotInput = toSlot.getValue();
        var userPositionInput = position.getValue();
        if (!userSlotInput.getId().equals(task.getSlot().getId())) {
            response = new Response(userSlotInput.getId(), userPositionInput - 1);
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) fromSlot.getScene().getWindow()).close();
    }

    private void updatePositionSpinner(Observable observable, SlotView oldVal, SlotView newVal) {
        position.setDisable(newVal.getId().equals(task.getSlot().getId()));
        var spinnerMaxVal = jfxService.getCardsInSlotCount(newVal.getId()) + 1;
        var spinnerOldVal = (position.getValue() != null) ? position.getValue() : spinnerMaxVal;
        var spinnerNewVal = Math.min(spinnerOldVal, spinnerMaxVal);
        position.setValueFactory(new IntegerSpinnerValueFactory(1, spinnerMaxVal, spinnerNewVal));
    }

    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Response {
        UUID slotId;
        int position;
    }
}
