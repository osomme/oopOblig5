package no.hiof.oskarsomme.model.windowlauncher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.hiof.oskarsomme.MainJavaFX;
import no.hiof.oskarsomme.controller.EditController;
import no.hiof.oskarsomme.controller.EpisodeListController;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;


public class WindowLauncher {
    public void goToEditDialog(Object obj, TVSeries series) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../view/PopupMenu.fxml"));

            Parent root = loader.load();

            Stage editWindow = new Stage();
            editWindow.initModality(Modality.WINDOW_MODAL);
            editWindow.initOwner(MainJavaFX.stage);

            Scene editScene = new Scene(root);
            editWindow.setScene(editScene);

            EditController editController = loader.getController();
            editController.setStage(editWindow);
            editController.setEditableObject(obj, series);

            editWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            fxmlLoadingErrorBox(e);
        }
    }

    public void goToEditDialog(Object obj) {
        goToEditDialog(obj, null);
    }

    public void goToEpisodeListing(TVSeries series) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../view/EpisodeList.fxml"));

            Parent root = loader.load();

            Stage episodeList = new Stage();
            episodeList.setMinWidth(520);
            episodeList.setMinHeight(410);
            episodeList.setTitle(series.getTitle() + " - episodelist");
            episodeList.initModality(Modality.WINDOW_MODAL);
            episodeList.initOwner(MainJavaFX.stage);

            EpisodeListController elc = loader.getController();
            elc.setTVSeries(series);

            Scene listScene = new Scene(root);
            episodeList.setScene(listScene);
            episodeList.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            fxmlLoadingErrorBox(e);
        }
    }

    public static void fxmlLoadingErrorBox(Exception e) {
        Alert errorBox = new Alert(Alert.AlertType.ERROR);
        errorBox.setTitle("FXML loading error");
        errorBox.setHeaderText(null);
        errorBox.setContentText(e.getMessage());
        errorBox.showAndWait();
    }

    public static void episodeAlreadyExistsErrorBox() {
        Alert duplicateErrorBox = new Alert(Alert.AlertType.ERROR);
        duplicateErrorBox.setTitle("Error");
        duplicateErrorBox.setHeaderText(null);
        duplicateErrorBox.setContentText("Episode has already been added");
        duplicateErrorBox.showAndWait();
    }
}
