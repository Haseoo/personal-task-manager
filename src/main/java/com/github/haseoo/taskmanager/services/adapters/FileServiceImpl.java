package com.github.haseoo.taskmanager.services.adapters;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.haseoo.taskmanager.models.TaskList;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileServiceImpl {
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

    public boolean isOutputSaved() {
        return savedOutput != null;
    }
}
