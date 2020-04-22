package com.github.haseoo.taskmanager.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class TaskController {
    @FXML
    GridPane rightGrid;

    private Node taskInfoNode;

    @FXML
    void initialize() {
        taskInfoNode = rightGrid.getChildren()
                .stream()
                .filter(e -> e.getId().equals("moreInfo")).findAny()
                .orElseThrow(AssertionError::new);
        rightGrid.getChildren().remove(taskInfoNode);
    }

    @FXML
    void onTaskMove() {

    }

    @FXML
    void onTaskDelete() {

    }

    @FXML
    void showInfo() {
        rightGrid.add(taskInfoNode, 0, 1);
    }

    @FXML
    void hideInfo() {
        rightGrid.getChildren().remove(taskInfoNode);
    }
}
