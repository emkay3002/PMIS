package com.example.backup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class SceneLoader {

    /**
     * Loads a new scene from the specified FXML file and sets it on the given stage.
     *
     * @param fxmlPath The path to the FXML file to load.
     * @param stage The stage on which to set the new scene.
     */
    public static void loadScene(String fxmlPath, Stage stage) {
        try {
            // Ensure the path is correct and relative to the class location.
            FXMLLoader fxmlLoader = new FXMLLoader(SceneLoader.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            // Set the new scene on the provided stage.
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            // Show an alert to the user in case of an error.
            showAlert("Error", "Unable to load the scene from " + fxmlPath, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();

            // Handle any other exceptions.
            showAlert("Error", "An unexpected error occurred", e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the provided title, header, and content.
     *
     * @param title The title of the alert dialog.
     * @param header The header text of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
