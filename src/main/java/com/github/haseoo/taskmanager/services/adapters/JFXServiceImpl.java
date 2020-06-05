package com.github.haseoo.taskmanager.services.adapters;

import com.github.haseoo.taskmanager.controllers.MainWindowController;
import com.github.haseoo.taskmanager.controllers.SlotController;
import com.github.haseoo.taskmanager.controllers.TaskController;
import com.github.haseoo.taskmanager.data.*;
import com.github.haseoo.taskmanager.models.Slot;
import com.github.haseoo.taskmanager.models.Tag;
import com.github.haseoo.taskmanager.models.Task;
import com.github.haseoo.taskmanager.models.TaskList;
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
import java.util.function.Predicate;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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

    public TaskList getModel() {
        return taskListService.prepareModels();
    }

    public void openUrl(URLs url) {
        application.getHostServices().showDocument(url.getUrl());
    }

    public TaskListData getCurrentTaskList() {
        return taskListService.getCurrentTaskList();
    }

    public void loadFromModel(TaskList taskList) throws IOException {
        validateIntegrity(taskList);
        newList();
        getCurrentTaskList().setName(taskList.getName());
        loadTags(taskList);
        loadSlots(taskList);
        loadTasks(taskList);
        loadTemplates(taskList);
    }

    public void newList() {
        taskListService.loadNewList();
        slotHBox.getChildren().clear();
        slots.clear();
        tasks.clear();
        slotCards.clear();
    }

    public void addNewSlot() throws IOException {
        var newSlot = SlotData.defaultInstance();
        addSlot(newSlot);
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
        addTask(slot, task);
    }

    public void removeTask(UUID taskId) {
        var task = taskListService.getTaskById(taskId);
        var taskController = tasks.get(taskId);
        slotCards.get(task.getSlot().getId()).remove(taskController.getCurrentCard());
        taskListService.removeTask(taskId);
    }

    public void moveTaskToPosition(UUID taskId, int position) {
        var task = taskListService.getTaskById(taskId);
        var card = tasks.get(taskId).getCurrentCard();
        var slot = slotCards.get(task.getSlot().getId());
        slot.remove(card);
        slot.add(position, card);
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

    public void setTaskTag(UUID taskId, UUID tagId) {
        var task = taskListService.getTaskById(taskId);
        if (tagId != null) {
            task.setTag(taskListService.getTagById(tagId));
        } else {
            task.setTag(null);
        }
    }

    public void updateTaskCompletenessOnCard(UUID taskId, double value) {
        String stringValue;
        if (Double.isNaN(value)) {
            stringValue = "";
        } else {
            stringValue = String.format("%.2f%%", 100.0 * value);
        }
        tasks.get(taskId).updateCompleteness(stringValue, value >= 1.0);
    }

    public List<TaskData> getTasksByCondition(Predicate<TaskData> condition) {
        return taskListService.getTasks().stream().filter(condition).collect(toList());
    }

    public TaskData getTaskById(UUID id) {
        return taskListService.getTaskById(id);
    }

    public List<TaskTemplateData> getTaskTemplates() {
        return taskListService.getTaskTemplates();
    }

    public TaskTemplateData getTemplateById(UUID id) {
        return taskListService.getTemplateById(id);
    }

    public void addTaskTemplate(TaskTemplateData taskTemplate) {
        taskListService.addTaskTemplate(taskTemplate);
    }

    public void removeTaskTemplate(UUID id) {
        taskListService.removeTaskTemplate(id);
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

    private void validateIntegrity(TaskList taskList) {
        var taskSlotMap = taskList.getTasks().stream()
                .collect(groupingBy(Task::getSlotId));
        var taskTagMap = taskList.getTasks().stream()
                .filter(task -> task.getTagId() != null)
                .collect(groupingBy(Task::getTagId));
        for (var entry : taskSlotMap.keySet()) {
            if (taskList.getSlots().stream()
                    .map(Slot::getId)
                    .noneMatch(id -> id.equals(entry))) {
                throw new AssertionError();
            }
        }
        for (var entry : taskTagMap.keySet()) {
            if (taskList.getTags().stream()
                    .map(Tag::getId)
                    .noneMatch(id -> id.equals(entry))) {
                throw new AssertionError();
            }
        }
    }

    private void addSlot(SlotData slot) throws IOException {
        var controller = new SlotController(this, slot);
        GridPane loadedPane = loadSlot(controller);
        controller.setSlotNode(loadedPane);
        putSlot(slot, controller, loadedPane);
        slot.setPositionSupplier(this::getSlotPosition);
    }

    private void addTask(SlotData slot, TaskData task) throws IOException {
        var taskController = new TaskController(this, task);
        GridPane card = loadCard(taskController);
        taskController.setCurrentCard(card);
        putNewCard(slot, task, taskController, card);
        task.setPositionSupplier(this::getTaskPosition);
    }

    private void loadTags(TaskList taskList) {
        taskList.getTags().stream().map(TagData::from).forEach(taskListService::addTag);
    }

    private void loadSlots(TaskList taskList) throws IOException {
        taskList.getSlots().sort(Comparator.comparingInt(Slot::getPosition));
        for (Slot slot : taskList.getSlots()) {
            SlotData from = SlotData.from(slot);
            addSlot(from);
        }
    }

    private void loadTasks(TaskList taskList) throws IOException {
        var taskSlotMap = taskList.getTasks().stream()
                .collect(groupingBy(Task::getSlotId));
        for (var entry : taskSlotMap.entrySet()) {
            var taskModelList = taskSlotMap.get(entry.getKey());
            taskModelList.sort(Comparator.comparingInt(Task::getPosition));
            for (var taskModel : taskModelList) {
                var taskData = TaskData.from(taskModel);
                var slot = taskListService.getSlotById(taskModel.getSlotId());
                addTask(slot, taskData);
                taskData.setSlot(slot);
                if (taskModel.getTagId() != null) {
                    taskData.setTag(taskListService.getTagById(taskModel.getTagId()));
                }
                updateTaskCompletenessOnCard(taskData.getId(), taskData.getCompleteness() / 100.0);
            }
        }
    }

    private void loadTemplates(TaskList taskList) {
        var templates = taskList.getTemplates().stream().map(TaskTemplateData::from).collect(toList());
        for (var template : templates) {
            addTaskTemplate(template);
        }
    }
}
