package com.github.haseoo.taskmanager.services.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.haseoo.taskmanager.controllers.MainWindowController;
import com.github.haseoo.taskmanager.controllers.SlotController;
import com.github.haseoo.taskmanager.controllers.TaskController;
import com.github.haseoo.taskmanager.data.*;
import com.github.haseoo.taskmanager.services.ports.TaskListService;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import com.github.haseoo.taskmanager.utilities.URLs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.*;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;

@RequiredArgsConstructor
public class JFXServiceImpl {
    private final Application application;
    private final TaskListService taskListService;

    private Map<UUID, SlotController> slots = new HashMap<>();
    private Map<UUID, List<Node>> slotCards = new HashMap<>();
    private Map<UUID, TaskController> tasks = new HashMap<>();

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

    public List<SlotData> getSlots() {
        return taskListService.getSlots();
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

    public void onSlotInitialization(UUID slotId, List<Node> cards) {
        slotCards.put(slotId, cards);
    }

    public void addNewTask(UUID slotId) throws IOException {
        var slot = taskListService.getSlotById(slotId);
        var task = TaskData.defaultInstance(slot);
        var taskController = new TaskController(this, task);
        GridPane card = loadCard(taskController);
        taskController.setCurrentCard(card);
        putNewCard(slot, task, taskController, card);
        task.setPositionSupplier(this::getTaskPosition);
    }

    public void removeTask(UUID taskId) {
        var task = taskListService.getTaskById(taskId);
        var taskController = tasks.get(taskId);
        slotCards.get(task.getSlot().getId()).remove(taskController.getCurrentCard());
        taskListService.removeTask(taskId);
    }

    public void moveTaskToSlot(UUID taskId, UUID slotId, int position) {
        var task = taskListService.getTaskById(taskId);
        var card = tasks.get(taskId).getCurrentCard();
        slotCards.get(task.getSlot().getId()).remove(card);
        slotCards.get(slotId).add(position, card);
        taskListService.moveTask(position, taskId, slotId);
    }

    public int getCardsInSlotCount(UUID slotId) {
        return slotCards.get(slotId).size();
    }

    public void addTag(String name, TagColorData color) {
        taskListService.addTag(TagData.newInstance(name, color));
    }

    public List<TagData> getTags() {
        return taskListService.getTags();
    }

    public void updateTag(UUID id, String name, TagColorData color) {
        var tag = taskListService.getTagById(id);
        tag.setName(name);
        tag.setTagColor(color);
    }

    public void removeTag(UUID id) {
        taskListService.deleteTag(id);
    }

    public long getTagTaskCount(UUID tagId) {
        return taskListService.getTasks()
                .stream()
                .filter(task -> task.getTag() != null &&
                        task.getTag().getId().equals(tagId))
                .count();
    }


    private int getTaskPosition(TaskData task) {
        var taskController = tasks.get(task.getId());
        return slotCards.get(task.getSlot().getId()).indexOf(taskController.getCurrentCard());
    }

    private void putNewCard(SlotData slot, TaskData task, TaskController taskController, GridPane card) {
        slot.getTasks().add(task);
        tasks.put(task.getId(), taskController);
        slotCards.get(slot.getId()).add(card);
    }

    private GridPane loadCard(TaskController taskController) throws IOException {
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.TASK_CARD));
        loader.setController(taskController);
        return loader.load();
    }

    private void putSlot(SlotData newSlot, SlotController controller, GridPane loadedPane) {
        slots.put(newSlot.getId(), controller);
        taskListService.addSlot(newSlot);
        slotHBox.getChildren().addAll(loadedPane);
    }

    private GridPane loadSlot(SlotController controller) throws IOException {
        var loader = new FXMLLoader(getResourceURL(FxmlFilePaths.SLOT));
        loader.setController(controller);
        return loader.load();
    }

}
