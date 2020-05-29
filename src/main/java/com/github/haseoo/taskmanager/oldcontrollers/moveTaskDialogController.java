package com.github.haseoo.taskmanager.oldcontrollers;

import com.github.haseoo.taskmanager.oldcontrollers.views.SlotComboBoxView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class moveTaskDialogController {
    private final SlotComboBoxView sourceSlot;
    private final List<SlotComboBoxView> slots;

    private SlotComboBoxView response;

    public Optional<SlotComboBoxView> getResponse() {
        return Optional.ofNullable(response);
    }

    @FXML
    private Label fromSlot;
    @FXML
    private ComboBox<SlotComboBoxView> toSlot;

    @FXML
    void initialize() {
        fromSlot.textProperty().setValue(sourceSlot.getName());
        toSlot.getItems().addAll(slots);
        toSlot.setValue(sourceSlot);
    }

    @FXML
    void onCancel() {
        closeWindow();
    }

    @FXML
    void onSave() {
        var responseFromInput = toSlot.getValue();
        if (!responseFromInput.equals(sourceSlot)) {
            response = responseFromInput;
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) fromSlot.getScene().getWindow()).close();
    }
}
