package com.github.haseoo.taskmanager.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

import static com.github.haseoo.taskmanager.utilities.URLs.REPORT_UNEXPECTED_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utilities {
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

    public static void showUserInputAlert(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect input");
        alert.setHeaderText("You've entered incorrect data");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSavedInfo() {
        new Alert(Alert.AlertType.INFORMATION, "File saved").showAndWait();
    }

    public static void showSavingError() {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File saving failed");
        alert.setHeaderText("An error occurred while saving a file");
        alert.setContentText("Try again");
        alert.showAndWait();
    }

    public static void showOpeningError() {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File opening failed");
        alert.setHeaderText("An error occurred while opening a file");
        alert.setContentText("It may not exit or it's corrupted");
        alert.showAndWait();
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @SneakyThrows
    public static void reportProblem(Throwable throwable) {
        var mapper = new ObjectMapper();
        String reportJSON = mapper.writeValueAsString(new Report(getStackTrace(throwable)));
        URL url = new URL(REPORT_UNEXPECTED_EXCEPTION.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(reportJSON);
        osw.flush();
        osw.close();
        connection.getResponseCode();
    }
}
