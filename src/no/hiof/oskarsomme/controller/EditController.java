package no.hiof.oskarsomme.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import no.hiof.oskarsomme.MainJavaFX;
import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.Episode;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;

import java.time.LocalDate;

public class EditController {
    @FXML
    private Tooltip yearTooltip, monthTooltip, dayTooltip, descriptionTooltip, runtimeTooltip, titleTooltip;
    @FXML
    private Label editHeader, runtimeLabel, labelPosterAndSeason, labelDescriptionAndEpNumber;
    @FXML
    private TextField inputDay, inputMonth, inputYear, inputTitle, inputRuntime, inputDescriptionAndEpNumber, inputPosterAndSeason;

    private Object editableObject;
    private Film film;
    private TVSeries series;
    private Episode episode;
    private Stage stage;

    @FXML
    private void initialize() {
        setTooltips();
    }

    public void setEditableObject(Object obj, TVSeries series) {
        this.editableObject = obj;
        if(obj.getClass() == Film.class) {
            this.film = (Film) obj;
            setEditableFilm(film);
        } else if (obj.getClass() == TVSeries.class) {
            this.series = (TVSeries) obj;
            setEditableTVSeries(this.series);
        } else {
            this.episode = (Episode) obj;
            this.series = series;
            setEditableEpisode(episode);
        }

        // Sets header text depending on whether the Edit or New button was pressed.
        setHeader();
        setTitle();
    }

    private void setEditableFilm(Film film) {
        if(!film.isEmpty()) {
            inputTitle.setText(film.getTitle());
            inputRuntime.setText(String.valueOf(film.getRunningTime()));
            inputDescriptionAndEpNumber.setText(film.getDescription());
            inputDay.setText(String.valueOf(film.getReleaseDate().getDayOfMonth()));
            inputMonth.setText(String.valueOf(film.getReleaseDate().getMonthValue()));
            inputYear.setText(String.valueOf(film.getReleaseDate().getYear()));
            inputPosterAndSeason.setText(film.getPosterURL());
        }
    }

    private void setEditableTVSeries(TVSeries series) {
        inputRuntime.setVisible(false);
        runtimeLabel.setVisible(false);

        if(!series.isEmpty()) {
            inputTitle.setText(series.getTitle());
            inputDescriptionAndEpNumber.setText(series.getDescription());
            inputDay.setText(String.valueOf(series.getReleaseDate().getDayOfMonth()));
            inputMonth.setText(String.valueOf(series.getReleaseDate().getMonthValue()));
            inputYear.setText(String.valueOf(series.getReleaseDate().getYear()));
            inputPosterAndSeason.setText(series.getPosterURL());
        }
    }

    private void setEditableEpisode(Episode episode) {
        // Sets text for label and inputfield for season-input.
        labelPosterAndSeason.setText("Season:");
        inputPosterAndSeason.setText(String.valueOf(episode.getSeason()));
        // Sets text for label and inputfield for episodenumber-input.
        labelDescriptionAndEpNumber.setText("Episode:");
        inputDescriptionAndEpNumber.setText(String.valueOf(episode.getEpisodeNumber()));

        if(!episode.isEmpty()) {
            inputTitle.setText(episode.getTitle());
            inputDay.setText(String.valueOf(episode.getReleaseDate().getDayOfMonth()));
            inputMonth.setText(String.valueOf(episode.getReleaseDate().getMonthValue()));
            inputYear.setText(String.valueOf(episode.getReleaseDate().getYear()));
            inputRuntime.setText(String.valueOf(episode.getRunningTime()));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void ok() {
        if(validInput()) {
            int day = getInteger(inputDay.getText());
            int month = getInteger(inputMonth.getText());
            int year = getInteger(inputYear.getText());

            if(editableObject.getClass() == Film.class) {
                film.setTitle(inputTitle.getText());
                film.setRunningTime(getInteger(inputRuntime.getText()));
                film.setReleaseDate(LocalDate.of(year, month, day));
                film.setDescription(inputDescriptionAndEpNumber.getText());
                film.setPosterURL(inputPosterAndSeason.getText());

                if(MainJavaFX.db.contains(film))
                    MainJavaFX.db.updateFilm(film);
                else
                    MainJavaFX.db.addFilm(film);

            } else if(editableObject.getClass() == TVSeries.class) {
                series.setTitle(inputTitle.getText());
                series.setReleaseDate(LocalDate.of(year, month, day));
                series.setDescription(inputDescriptionAndEpNumber.getText());
                series.setPosterURL(inputPosterAndSeason.getText());

                if(MainJavaFX.db.contains(series))
                    MainJavaFX.db.updateTVSeries(series);
                else
                    MainJavaFX.db.addTVSeries(series);
            } else {
                episode.setTitle(inputTitle.getText());
                episode.setSeason(getInteger(inputPosterAndSeason.getText()));
                episode.setEpisodeNumber(getInteger(inputDescriptionAndEpNumber.getText()));
                episode.setReleaseDate(LocalDate.of(year, month, day));
                episode.setRunningTime(getInteger(inputRuntime.getText()));

                if(MainJavaFX.db.contains(episode))
                    MainJavaFX.db.updateEpisode(episode);
                else
                    if(!MainJavaFX.db.addEpisode(episode, series))
                        // Removes episode from tv show list if SQL gives an error (due to database constraints)
                        series.getEpisodes().remove(episode);
            }
            stage.close();
        }
    }

    private boolean validInput() {
        // Starts by assuming that inputvalues are valid.
        boolean isValid = true;

        String title = inputTitle.getText();
        String description = inputDescriptionAndEpNumber.getText();
        int day = getInteger(inputDay.getText());
        int month = getInteger(inputMonth.getText());
        int year = getInteger(inputYear.getText());


        boolean titleLength = !title.isEmpty() && title.length() < 60;
        boolean descriptionLength = !description.isEmpty() && description.length() < 500;
        boolean validYear = year >= 1900 && year < LocalDate.now().getYear() + 4;

        if(editableObject.getClass() == Film.class) {
            int runningTime = getInteger(inputRuntime.getText());
            boolean runTimeLength = runningTime > 0 && runningTime < 1000;
            if(!runTimeLength) {
                setInputFieldColor(inputRuntime, "red");
                isValid = false;
            } else {
                setInputFieldColor(inputRuntime, "green");
            }
        }

        if(!titleLength) {
            setInputFieldColor(inputTitle, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputTitle, "green");
        }

        if(!descriptionLength) {
            setInputFieldColor(inputDescriptionAndEpNumber, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputDescriptionAndEpNumber, "green");
        }

        // Check to see if date is valid
        if(!validDate(year, month, day)) {
            isValid = false;
        }

        // Checks to see if input-year is valid.
        if(!validYear) {
            alertBox("Year must be > 1900 and < " + (LocalDate.now().getYear() + 4));
            setInputFieldColor(inputYear, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputYear, "green");
        }

        return isValid;
    }

    private void alertBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private int getInteger(String str) {
        try {
            if(str.isEmpty()) {
                return 0;
            } else {
                return Integer.parseInt(str);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setInputFieldColor(TextField field, String color) {
        field.setStyle("-fx-border-color: " + color + ";");
    }

    private void setHeader() {
        if(editableObject.getClass() == Film.class) {
            if(film.isEmpty())
                editHeader.setText("Create new film:");
            else
                editHeader.setText("Edit \"" + film.getTitle() + " (" + film.getReleaseDate().getYear() + ")\":");

        } else if(editableObject.getClass() == TVSeries.class) {
            if(series.isEmpty())
                editHeader.setText("Create new TV series:");
            else
                editHeader.setText("Edit \"" + series.getTitle() + "\"");

        } else {
            if(episode.isEmpty())
                editHeader.setText("Create new episode:");
            else
                editHeader.setText("Edit \"" + episode.getTitle() + "\"");
        }
    }

    private boolean validDate(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            dateErrorSetColor(e);
            return false;
        }

        return true;
    }

    private void dateErrorSetColor(Exception e) {
        if(e.getMessage().contains("MonthOfYear")) {
            setInputFieldColor(inputMonth, "red");
        } else {
            setInputFieldColor(inputMonth, "green");

            if (e.getMessage().contains("DayOfMonth")) {
                setInputFieldColor(inputDay, "red");
            } else {
                setInputFieldColor(inputDay, "green");
            }
        }
    }

    private void setTooltips() {
        Tooltip[] tooltips = {yearTooltip, monthTooltip, dayTooltip, descriptionTooltip, runtimeTooltip, titleTooltip};

        for(Tooltip tooltip : tooltips) {
            // Time before tooltip shows (in ms)
            tooltip.setShowDelay(new Duration(150));
            // Time before tooltip closes (in ms)
            tooltip.setHideDelay(new Duration(1));
        }

        // Sets tooltip text for year input box
        yearTooltip.setText("Must be a year between 1900 and " + (LocalDate.now().getYear() + 4));
    }

    private void setTitle() {
        if(editableObject.getClass() == Film.class) {
            if(film.isEmpty())
                stage.setTitle("New film");
            else
                stage.setTitle("Edit film");
        } else if(editableObject.getClass() == TVSeries.class) {
            if(series.isEmpty())
                stage.setTitle("New TV Series");
            else
                stage.setTitle("Edit TV series");
        } else {
            if(episode.isEmpty())
                stage.setTitle("New episode");
            else
                stage.setTitle("Edit episode");
        }
    }

    @FXML
    private void cancel() {
        stage.close();
    }
}
