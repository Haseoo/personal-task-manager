package com.github.haseoo.taskmanager.utilities;

import com.github.haseoo.taskmanager.FXMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utilities {
    public static URL getResourceURL(String relativePath) {
        return Objects.requireNonNull(FXMain.class.getClassLoader().getResource(relativePath));
    }

    public static String getErrorMessageForDialog(Throwable throwable) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(throwable.getMessage());
        var cause = throwable.getCause();
        while (cause != null) {
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append(cause.getMessage());
            cause = cause.getCause();
        }
        return stringBuilder.toString();
    }

    public static Stage prepareDialog(Parent root, String dialogTitle) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        dialog.setTitle(dialogTitle);
        dialog.setScene(scene);
        return dialog;
    }

    public static Stage prepareWindow(String relPath, String windowTitle, Object controller) throws IOException {
        var loader = new FXMLLoader(getResourceURL(relPath));
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(windowTitle);
        stage.show();
        return stage;
    }

    public static void textInputDialog(String value, String title, String header, Consumer<String> action) {
        var dialog = new TextInputDialog(value);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.showAndWait().ifPresent(action);
    }

    public static void confirmationDialog(String header, String content, Runnable action) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conform operation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                action.run();
            }
        });
    }

    public static void deleteConfirmationDialog(String header, Runnable action) {
        confirmationDialog(header, "This operation is irreversible!", action);
    }
}
