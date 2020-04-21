package com.github.haseoo.taskmanager.utilities;

import com.github.haseoo.taskmanager.FXMain;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utilities {
    public static URL getResourceURL(String relativePath) {
        return Objects.requireNonNull(FXMain.class.getClassLoader().getResource(relativePath));
    }

    public static String getErrorMessage(Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(throwable.getMessage());
        Throwable cause = throwable.getCause();
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
        dialog.showAndWait();
        return dialog;
    }
}
