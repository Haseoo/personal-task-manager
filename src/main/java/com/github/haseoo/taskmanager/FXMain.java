package com.github.haseoo.taskmanager;

import com.github.haseoo.taskmanager.controllers.MainWindowController;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.services.adapters.TaskListServiceImpl;
import com.github.haseoo.taskmanager.services.ports.TaskListService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;

public class FXMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL("FXML/mainWindow.fxml"));
        //mainWindow.setController(new MainWindowController(this, TaskListView.DEFAULT_VALUE));
        TaskListService taskListService = new TaskListServiceImpl();
        JFXServiceImpl jfxService = new JFXServiceImpl(this, taskListService);
        mainWindow.setController(new MainWindowController(jfxService));
        Parent root = mainWindow.load();


        Scene scene = new Scene(root);

        stage.setTitle("Personal task manager");
        stage.setScene(scene);
        stage.show();
    }
}
