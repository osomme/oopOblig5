package no.hiof.oskarsomme;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.hiof.oskarsomme.controller.FilmEditController;
import no.hiof.oskarsomme.controller.FilmOverviewController;
import no.hiof.oskarsomme.datamanagement.Datamanagement;
import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;

import java.time.LocalDate;

public class MainJavaFX extends Application {
    private ObservableList<Film> films = FXCollections.observableArrayList();
    private ObservableList<TVSeries> tvSeries = FXCollections.observableArrayList();
    private Stage primaryStage;
    private FilmOverviewController filmOverviewController;

    public MainJavaFX() {
        Datamanagement.loadFromJson("films.json");

        TVSeries doul = new TVSeries("Days of Our Lives", "A " +
                "chronicle of the lives, loves, trials and tribulations of " +
                "the citizens of the fictional city of Salem.", LocalDate.of(1965, 11, 8));
        TVSeries arrestedDevelopment = new TVSeries("Arrested Development", "Level-headed" +
                " son Michael Bluth takes over family affairs after his father is imprisoned." +
                " But the rest of his spoiled, dysfunctional family are" +
                " making his job unbearable.", LocalDate.of(2003, 1, 1), "http://tvmedia.ign.com/tv/image/object/824/824061/arresteddevelopment_box2.jpg");

        tvSeries.addAll(doul, arrestedDevelopment);

        films.addAll(Film.LIST_OF_FILMS);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        goToFilmListing();
    }

    @Override
    public void stop() {
        Datamanagement.saveToJson("films.json", films);
    }

    private void goToFilmListing() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/FilmOverview.fxml"));
            Parent root = loader.load();

            filmOverviewController = loader.getController();
            filmOverviewController.setMain(this);

            Scene primaryScene = new Scene(root);

            primaryStage.setTitle("Filmer");

            primaryStage.setMinHeight(500);
            primaryStage.setMinWidth(500);
            primaryStage.setScene(primaryScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            fxmlLoadingErrorBox(e);
        }
    }

    public void goToEditDialog(Film film) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/PopupMenu.fxml"));

            Parent root = loader.load();

            Stage editMenu = new Stage();
            editMenu.setTitle("Edit film");
            editMenu.initModality(Modality.WINDOW_MODAL);
            editMenu.initOwner(primaryStage);

            Scene editScene = new Scene(root);
            editMenu.setScene(editScene);

            FilmEditController fec = loader.getController();
            fec.setStage(editMenu);
            fec.setFilm(film);
            editMenu.showAndWait();

            /* After dialogbox is closed: */

            // Updates info in FilmOverview.
            filmOverviewController.setInfoListing(film);
            // Forces a refresh of the list in FilmOverview.
            filmOverviewController.getFilmListView().refresh();
        } catch (Exception e) {
            e.printStackTrace();
            fxmlLoadingErrorBox(e);
        }
    }

    public ObservableList<Film> getFilms() {
        return films;
    }

    public ObservableList<TVSeries> getTVSeries() {
        return tvSeries;
    }

    public void addFilm() {
        Film newFilm = new Film();
        goToEditDialog(newFilm);
        if(!newFilm.isEmpty()) {
            films.add(newFilm);
        }
    }

    private void fxmlLoadingErrorBox(Exception e) {
        Alert errorBox = new Alert(Alert.AlertType.ERROR);
        errorBox.setTitle("Something went wrong...");
        errorBox.setHeaderText(null);
        errorBox.setContentText(e.getMessage());
        errorBox.showAndWait();
    }
}
