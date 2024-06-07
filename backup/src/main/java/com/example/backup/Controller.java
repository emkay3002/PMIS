package com.example.backup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Controller {
    @FXML
    private Button transportButton;

    @FXML
    private Button maintenanceButton;

    @FXML
    private Button communityButton;

    @FXML
    private Hyperlink aboutLink;

    @FXML
    private Hyperlink contactLink;

    @FXML
    private Hyperlink helpLink;

    // Action for maintenance button click
    @FXML
    private void handleFacilityMaintenanceButtonClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Facility.fxml"));
            Stage stage = (Stage) transportButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action for transport button click
    public void handleTransportButtonClick(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Transport.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) transportButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action for community service button click
    public void handleCommunityServiceButtonClick(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CommunityService.fxml"));
            Stage stage = (Stage) transportButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action for the About hyperlink click
    @FXML
    private void handleAboutLinkClick(javafx.event.ActionEvent event) {
        String url = "https://docs.google.com/document/d/1HcwA8HiXEOAR_ChuKpxNFgL6HjOz49MLhW6GsgMVv_0/edit";
        openWebpage(url);
    }

    // Action for the Contact Us hyperlink click
    @FXML
    private void handleContactLinkClick(javafx.event.ActionEvent event) {
        String url = "https://www.linkedin.com/in/eman-khalid-b5b3a4216/";
        openWebpage(url);
    }

    // Action for the Help hyperlink click
    @FXML
    private void handleHelpLinkClick(javafx.event.ActionEvent event) {
        String url = "https://github.com/emkay3002/PMIS";
        openWebpage(url);
    }

    // Helper method to open a webpage
    private void openWebpage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop is not supported. Cannot open URL.");
        }
    }
}
