package com.github.haseoo.taskmanager;

import com.github.haseoo.taskmanager.controllers.MainWindowController;
import com.github.haseoo.taskmanager.services.adapters.FileServiceImpl;
import com.github.haseoo.taskmanager.services.adapters.JFXServiceImpl;
import com.github.haseoo.taskmanager.services.adapters.TaskListServiceImpl;
import com.github.haseoo.taskmanager.services.ports.TaskListService;
import com.github.haseoo.taskmanager.utilities.FxmlFilePaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.github.haseoo.taskmanager.utilities.Constants.APPLICATION_NAME;
import static com.github.haseoo.taskmanager.utilities.Utilities.getResourceURL;
import static com.github.haseoo.taskmanager.utilities.Utilities.getStackTrace;

@Slf4j
public class FXMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var mainWindow = new FXMLLoader(getResourceURL(FxmlFilePaths.MAIN_WINDOW));
        Thread.setDefaultUncaughtExceptionHandler(FXMain::handleUncaughtExceptions);
        TaskListService taskListService = new TaskListServiceImpl();
        var fileService = new FileServiceImpl();
        var jfxService = new JFXServiceImpl(this, taskListService);
        mainWindow.setController(new MainWindowController(jfxService, fileService));
        Parent root = mainWindow.load();
        Scene scene = new Scene(root);
        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.show();
    }

    private static void handleUncaughtExceptions(Thread thread, Throwable throwable) {
        if (!(throwable instanceof IOException)) {
            //reportProblem(throwable);
        }
        log.error(getStackTrace(throwable));
    }
}
