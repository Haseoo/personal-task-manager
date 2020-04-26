package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.controllers.views.tasklist.SlotView;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.OptionalInt;

@RequiredArgsConstructor
public class SlotDialogController {
    private final SlotView slot;
    private final int positionMaxValue;

    private String newName;
    private int newPosition = -1;

    @FXML
    private TextField slotName;

    @FXML
    private Spinner<Integer> slotPosition;

    public Optional<String> getNewName() {
        return Optional.ofNullable(newName);
    }

    public OptionalInt getNewPosition() {
        if (newPosition == -1) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(newPosition);
        }
    }

    @FXML
    void initialize() {
        slotName.setText(slot.getName().getValueSafe());
        var spinnerValue = new IntegerSpinnerValueFactory(
                1,
                positionMaxValue,
                slot.getPosition().getValue() + 1);
        slotPosition.setValueFactory(spinnerValue);
    }

    @FXML
    void onCancel() {
        closeWindow();
    }

    @FXML
    void onSave() {
        var checkName = slotName.getText();
        if (checkName != null && !checkName.equals("") && !checkName.equals(slot.getName().getValue())) {
            newName = checkName;
        }

        int checkPosition = slotPosition.getValue() - 1;
        if (checkPosition != slot.getPosition().get()) {
            newPosition = checkPosition;
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) slotName.getScene().getWindow()).close();
    }
}
