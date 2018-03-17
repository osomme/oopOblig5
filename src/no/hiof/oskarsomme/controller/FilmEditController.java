package no.hiof.oskarsomme.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import no.hiof.oskarsomme.model.media.film.Film;
import java.time.LocalDate;

public class FilmEditController {
    @FXML
    private Tooltip yearTooltip, monthTooltip, dayTooltip, descriptionTooltip, runtimeTooltip, titleTooltip;
    @FXML
    private Label editHeader;
    @FXML
    private TextField inputDay, inputMonth, inputYear, inputTitle, inputRuntime, inputDescription;

    private Film film;
    private Stage stage;

    @FXML
    private void initialize() {
        setTooltips();
    }

    public void setFilm(Film film) {
        this.film = film;

        if(!film.isEmpty()) {
            inputTitle.setText(film.getTitle());
            inputRuntime.setText(String.valueOf(film.getRunningTime()));
            inputDescription.setText(film.getDescription());
            inputDay.setText(String.valueOf(film.getReleaseDate().getDayOfMonth()));
            inputMonth.setText(String.valueOf(film.getReleaseDate().getMonthValue()));
            inputYear.setText(String.valueOf(film.getReleaseDate().getYear()));
        }

        // Sets header text depending on whether the Edit or New button was pressed.
        setHeader();
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

            film.setTitle(inputTitle.getText());
            film.setRunningTime(getInteger(inputRuntime.getText()));
            film.setReleaseDate(LocalDate.of(year, month, day));
            film.setDescription(inputDescription.getText());

            stage.close();
        }
    }

    private boolean validInput() {
        boolean isValid = true;

        String title = inputTitle.getText();
        String description = inputDescription.getText();
        int runningTime = getInteger(inputRuntime.getText());
        int day = getInteger(inputDay.getText());
        int month = getInteger(inputMonth.getText());
        int year = getInteger(inputYear.getText());


        boolean titleLength = !title.isEmpty() && title.length() < 60;
        boolean descriptionLength = !description.isEmpty() && description.length() < 300;
        boolean runTimeLength = runningTime > 0 && runningTime < 1000;
        boolean validYear = year >= 1900 && year < LocalDate.now().getYear() + 4;

        if(!titleLength) {
            setInputFieldColor(inputTitle, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputTitle, "green");
        }


        if(!runTimeLength) {
            setInputFieldColor(inputRuntime, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputRuntime, "green");
        }

        if(!descriptionLength) {
            setInputFieldColor(inputDescription, "red");
            isValid = false;
        } else {
            setInputFieldColor(inputDescription, "green");
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
        if(film.isEmpty()) {
            editHeader.setText("Create new film:");
        } else {
            editHeader.setText("Edit \"" + film.getTitle() + " (" + film.getReleaseDate().getYear() + ")\":");
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

    @FXML
    private void cancel() {
        stage.close();
    }
}
