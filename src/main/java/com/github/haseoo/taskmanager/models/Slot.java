package com.github.haseoo.taskmanager.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private UUID id;
    private String name;
    private int position;

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"categories\":[{\"id\":\"34ab40e7-d2a3-485e-9f8e-afd1955a2820\",\"name\":\"ovo\",\"position\":1},{\"id\":\"61f130c7-13a6-434a-a710-059e7282e328\",\"name\":\"uwu\",\"position\":2}]}";
        /*TaskList taskList = TaskList.newInstance();
        taskList.getCategories().add(new Category(UUID.randomUUID(), "ovo", 1));
        taskList.getCategories().add(new Category(UUID.randomUUID(), "uwu", 2));*/
        TaskList taskList = objectMapper.readValue(json, TaskList.class);
        // System.out.println(objectMapper.writeValueAsString(taskList));
        //System.out.println(taskList);
        System.out.println(objectMapper.writeValueAsString(Color.RED));
    }
}
