package com.github.haseoo.taskmanager.controllers;

import com.github.haseoo.taskmanager.data.TagColorData;
import com.github.haseoo.taskmanager.views.TagView;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Optional;

import static com.github.haseoo.taskmanager.utilities.Constants.DEFAULT_COLOR;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
public class TagDialogController {
    private Response response;

    @FXML
    private TextField name;
    @FXML
    private ColorPicker color;

    private TagView tag;

    public TagDialogController(TagView tag) {
        this.tag = tag;
    }

    @FXML
    private void initialize() {
        if (tag == null) {
            color.setValue(Color.valueOf(DEFAULT_COLOR));
        } else {
            name.setText(tag.getName());
            color.setValue(tag.getColor());
        }
    }

    public Optional<Response> getResponse() {
        return Optional.ofNullable(response);
    }

    @FXML
    private void onSave() {
        var nameResponse = name.getText();
        var colorResponse = color.getValue();
        if (nameResponse != null && !nameResponse.equals("")) {
            response = new Response(nameResponse, TagColorData.newInstance(colorResponse));
        }
        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class Response {
        String name;
        TagColorData color;
    }

    private void closeWindow() {
        ((Stage) name.getScene().getWindow()).close();
    }
}
