package no.hiof.oskarsomme;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.hiof.oskarsomme.controller.FilmOverviewController;
import no.hiof.oskarsomme.datamanagement.MediaDatabaseManager;
import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.EpisodeGenerator;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;
import no.hiof.oskarsomme.model.windowlauncher.WindowLauncher;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainJavaFX extends Application {
    private ObservableList<Film> films = FXCollections.observableArrayList();
    private ObservableList<TVSeries> tvSeries = FXCollections.observableArrayList();
    public static Stage stage;
    public static MediaDatabaseManager db;

    public MainJavaFX() {
        //Datamanagement.loadFilmsFromJSON("src/no/hiof/oskarsomme/data/films.json");

        try {
            db = new MediaDatabaseManager(DriverManager.getConnection("jdbc:sqlite:src/no/hiof/oskarsomme/data/media.db"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.loadFilms();
        db.loadTVSeries();

        tvSeries.addAll(TVSeries.listOfTVseries);
        films.addAll(Film.listOfFilms);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        goToFilmListing();
    }

    @Override
    public void stop() {
        //Datamanagement.saveFilmsToJSON("src/no/hiof/oskarsomme/data/films.json", films);
        db.disconnect();
    }

    private void goToFilmListing() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/FilmOverview.fxml"));
            Parent root = loader.load();

            FilmOverviewController filmOverviewController = loader.getController();
            filmOverviewController.setMain(this);

            Scene primaryScene = new Scene(root);

            stage.setTitle("Filmer");

            stage.setMinHeight(500);
            stage.setMinWidth(500);
            stage.setScene(primaryScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            WindowLauncher.fxmlLoadingErrorBox(e);
        }
    }

    public ObservableList<Film> getFilms() {
        return films;
    }

    public ObservableList<TVSeries> getTVSeries() {
        return tvSeries;
    }
}
