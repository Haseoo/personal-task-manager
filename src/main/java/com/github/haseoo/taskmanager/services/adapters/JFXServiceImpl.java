package com.github.haseoo.taskmanager.services.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.haseoo.taskmanager.controllers.MainWindowController;
import com.github.haseoo.taskmanager.controllers.SlotController;
import com.github.haseoo.taskmanager.data.SlotData;
import com.github.haseoo.taskmanager.data.TaskListData;
import com.github.haseoo.taskmanager.services.ports.TaskListService;
import com.github.haseoo.taskmanager.utilities.URLs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;

@RequiredArgsConstructor
public class JFXServiceImpl {
    private final Application application;
    private final TaskListService taskListService;

    private Map<UUID, SlotController> slots = new HashMap<>();

    @Setter
    private MainWindowController mainWindowController;
    @Setter
    private HBox slotHBox;

    public void saveList() throws IOException {
        var model = taskListService.prepareModels();
        var mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(model));
    }

    public void openUrl(URLs url) {
        application.getHostServices().showDocument(url.getUrl());
    }

    public TaskListData getCurrentTaskList() {
        return taskListService.getCurrentTaskList();
    }

    public void addSlot() throws IOException {
        var newSlot = SlotData.defaultInstance();
        var controller = new SlotController(this, newSlot);
        GridPane loadedPane = loadSlot(controller);
        controller.setSlotNode(loadedPane);
        putSlot(newSlot, controller, loadedPane);
        newSlot.setPositionSupplier(this::getSlotPosition);
    }

    public int getSlotPosition(UUID slotId) {
        return slotHBox.getChildren().indexOf(slots.get(slotId).getSlotNode());
    }

    public void deleteSlot(UUID id) {
        var slotController = Objects.requireNonNull(slots.get(id));
        slotHBox.getChildren().remove(slotController.getSlotNode());
        taskListService.deleteSlot(id);
    }

    public int getSlotCount() {
        return slotHBox.getChildren().size();
    }

    public void moveSlot(UUID id, int position) {
        var controller = slots.get(id);
        slotHBox.getChildren().remove(controller.getSlotNode());
        slotHBox.getChildren().add(position, controller.getSlotNode());
    }


    private void putSlot(SlotData newSlot, SlotController controller, GridPane loadedPane) {
        slots.put(newSlot.getId(), controller);
        taskListService.addSlot(newSlot);
        slotHBox.getChildren().addAll(loadedPane);
    }

    private GridPane loadSlot(SlotController controller) throws IOException {
        var loader = new FXMLLoader(getResourceURL("FXML/slot.fxml"));
        loader.setController(controller);
        return loader.load();
    }

}
