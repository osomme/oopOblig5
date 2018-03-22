package no.hiof.oskarsomme.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import no.hiof.oskarsomme.MainJavaFX;
import no.hiof.oskarsomme.model.media.tvseries.Episode;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;
import no.hiof.oskarsomme.model.windowlauncher.WindowLauncher;

import java.time.LocalDate;


public class EpisodeListController {
    @FXML
    private TableColumn<Episode, String> columnTitle;
    @FXML
    private TableColumn<Episode, Integer> columnSeason;
    @FXML
    private TableColumn<Episode, Integer> columnEpisode;
    @FXML
    private TableColumn<Episode, LocalDate> columnAirdate;
    @FXML
    private TableColumn<Episode, Integer> columnRuntime;
    @FXML
    private TableView<Episode> episodeTable;
    @FXML
    private Label header;
    @FXML
    private Button btnNew, btnEdit, btnDelete;


    private ObservableList<Episode> episodeObservableList = FXCollections.observableArrayList();
    private Episode currentTarget;
    private TVSeries series;

    @FXML
    private void initialize() {
        // Sets width of each column.
        columnTitle.prefWidthProperty().bind(episodeTable.widthProperty().multiply(0.3));
        columnSeason.prefWidthProperty().bind(episodeTable.widthProperty().multiply(0.17));
        columnEpisode.prefWidthProperty().bind(episodeTable.widthProperty().multiply(0.17));
        columnAirdate.prefWidthProperty().bind(episodeTable.widthProperty().multiply(0.2));
        columnRuntime.prefWidthProperty().bind(episodeTable.widthProperty().multiply(0.17));

        episodeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentTarget = newValue
        );

        episodeTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && currentTarget != null) {
                editEpisode(currentTarget);
            }
        });

        btnNew.setOnAction(event -> {
            addEpisode();
            // Clunky workaround to fix table not refreshing when adding episode.
            episodeObservableList.removeAll(episodeObservableList);
            episodeObservableList.addAll(series.getEpisodes());
            setHeaderText("Total number of episodes: " + series.getNumberOfEpisodes());
        });

        btnEdit.setOnAction(event -> editEpisode(currentTarget));

        btnDelete.setOnAction(event -> {
            deleteEpisode(currentTarget);
            episodeTable.refresh();
        });
    }

    public void setTVSeries(TVSeries series) {
        episodeObservableList.addAll(series.getEpisodes());
        this.series = series;

        setHeaderText("Total number of episodes: " + series.getNumberOfEpisodes());

        setCellValues();
    }

    private void setCellValues() {
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        columnSeason.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeason()).asObject());
        columnEpisode.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEpisodeNumber()).asObject());
        columnAirdate.setCellValueFactory(cellData -> cellData.getValue().getReleaseDateProperty());
        columnRuntime.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRunningTime()).asObject());


        columnAirdate.setCellFactory(column -> new TableCell<>() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if(empty || date == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String month;
                    if(date.getMonthValue() < 10)
                        month = "0" + date.getMonthValue();
                    else
                        month = String.valueOf(date.getMonthValue());

                    setText(date.getDayOfMonth() +
                            "-" + month +
                            "-" + date.getYear());
                }
            }
        });

        episodeTable.setItems(episodeObservableList);
    }

    private void editEpisode(Episode episode) {
        new WindowLauncher().goToEditDialog(episode, series);
        episodeTable.refresh();
    }

    private void setHeaderText(String text) {
        header.setText(text);
    }

    private void addEpisode() {
        Episode newEpisode = new Episode();
        editEpisode(newEpisode);
        if(MainJavaFX.db.contains(newEpisode)) {
            series.addEpisode(newEpisode);
        }
    }

    private void deleteEpisode(Episode episode) {
        MainJavaFX.db.deleteEpisode(episode);
        episodeObservableList.remove(episode);
        series.getEpisodes().remove(episode);
    }
}
