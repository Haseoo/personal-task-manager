package com.github.haseoo.taskmanager.services.adapters;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.haseoo.taskmanager.models.TaskList;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileServiceImpl {
    @Setter
    private File savedOutput;

    public void reset() {
        savedOutput = null;
    }

    public void saveFileAs(File output, TaskList data) throws IOException {
        var mapper = new ObjectMapper();
        mapper.writer(new DefaultPrettyPrinter()).writeValue(output, data);
        savedOutput = output;
    }

    public void saveFile(TaskList data) throws IOException {
        var mapper = new ObjectMapper();
        mapper.writer(new DefaultPrettyPrinter()).writeValue(Objects.requireNonNull(savedOutput), data);
    }

    public TaskList openTaskList(File input) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.readValue(input, TaskList.class);

    }

    public boolean isOutputSaved() {
        return savedOutput != null;
    }
}
