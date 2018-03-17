package no.hiof.oskarsomme.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import no.hiof.oskarsomme.MainJavaFX;
import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;

import java.util.Collections;

public class FilmOverviewController {
    @FXML
    private RadioMenuItem contextSortTitleAsc, contextSortTitleDesc, contextSortReleaseAsc,
            contextSortReleaseDesc, contextSortRuntimeAsc, contextSortRuntimeDesc;
    @FXML
    private Button btnNew, btnEdit, btnDelete;
    @FXML
    private Label title, date, runtimeAndNumberOfSeasonsLabel, description;
    @FXML
    private Label runtimeAndNumberOfSeasonsDescription;
    @FXML
    private ListView<Film> filmList;
    @FXML
    private ListView<TVSeries> tvSeriesList;
    @FXML
    private ImageView poster;
    @FXML
    private TextField sortInput;

    private MainJavaFX main;
    private Film currentTarget;
    private ObservableList<Film> films;

    @FXML
    private void initialize() {
        // Edit film attributes (oppgave 9)
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                main.goToEditDialog(currentTarget);
                setInfoListing(currentTarget); // Updates info with new values
            }
        });

        // Add new film to list (oopgave 10).
        btnNew.setOnAction(event -> main.addFilm());

        // Delete from list (oppgave 11).
        btnDelete.setOnAction(event -> deleteFilm(currentTarget));

        // Handles actions involving context menu over listView.
        contextSortTitleAsc.setOnAction(event -> sortByTitleAscending());
        contextSortTitleDesc.setOnAction(event -> sortByTitleDescending());
        contextSortReleaseAsc.setOnAction(event -> sortByReleaseDateAscending());
        contextSortReleaseDesc.setOnAction(event -> sortByReleaseDateDescending());
        contextSortRuntimeAsc.setOnAction(event -> sortByRuntimeAscending());
        contextSortRuntimeDesc.setOnAction(event -> sortByRuntimeDescending());
    }


    public void setMain(MainJavaFX main) {
        this.main = main;

        // Sets items for Filmlist
        filmList.setItems(main.getFilms());
        filmList.setFixedCellSize(35);
        // Sets items for TV series list.
        tvSeriesList.setItems(main.getTVSeries());
        tvSeriesList.setFixedCellSize(35);

        // Sets text of listview cells.
        setListCells();

        // At launch the list is sorted by title (A-Z).
        films = main.getFilms();
        sortByTitleAscending();
        // Sets info with values of first film in list of films.
        setInfoListing(films.get(0));
        // Loads filtered search function.
        filteredSearch();
    }

    public void getInfo() {
        if(main.getFilms().size() > 0) {
            setInfoListing(filmList.getSelectionModel().getSelectedItem());
        }
    }

    public void setInfoListing(Object obj) {
        final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        try {
            if(obj.getClass() == Film.class) {
                Film film = (Film) obj;
                if(!film.isEmpty()) {
                    title.setText(film.getTitle());
                    date.setText(String.valueOf(film.getReleaseDate().getDayOfMonth() + " " +
                            MONTH_NAMES[film.getReleaseDate().getMonthValue() - 1] + " " +
                            film.getReleaseDate().getYear()));

                    runtimeAndNumberOfSeasonsDescription.setText("Running Time:");
                    runtimeAndNumberOfSeasonsLabel.setText(film.getRunningTime() + " minutes");
                    description.setText(film.getDescription());
                    poster.setImage(new loadImage(film).call());
                }
            } else if(obj.getClass() == TVSeries.class) {
                TVSeries tvSeries = (TVSeries) obj;
                title.setText(tvSeries.getTitle());
                date.setText(String.valueOf(tvSeries.getReleaseDate().getDayOfMonth() + " " +
                            MONTH_NAMES[tvSeries.getReleaseDate().getMonthValue() - 1] + " " +
                            tvSeries.getReleaseDate().getYear()));

                runtimeAndNumberOfSeasonsDescription.setText("Number of seasons:");
                runtimeAndNumberOfSeasonsLabel.setText(String.valueOf(tvSeries.getNumberOfEpisodes()));
                description.setText(tvSeries.getDescription());
                poster.setImage(new loadImage(tvSeries).call());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedContextMenu(RadioMenuItem item) {
        final RadioMenuItem[] RADIO_MENU_ITEMS = {contextSortTitleAsc, contextSortTitleDesc, contextSortReleaseAsc,
                contextSortReleaseDesc, contextSortRuntimeAsc, contextSortRuntimeDesc};

        for(RadioMenuItem menuItem : RADIO_MENU_ITEMS) {
            if(menuItem.getId().equals(item.getId())) {
                menuItem.setSelected(true);
            } else {
                menuItem.setSelected(false);
            }
        }
    }

    private void setListCells() {
        filmList.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            @Override
            public ListCell<Film> call(ListView<Film> filmListView) {
                return new FilmCell();
            }
        });


        filmList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Film>() {
                    @Override
                    public void changed(ObservableValue<? extends Film> observable, Film oldValue, Film newValue) {
                        if(newValue != null) {
                            currentTarget = newValue;
                            getInfo();
                        }
                    }
                }
        );
    }

    static class FilmCell extends ListCell<Film> {
        @Override
        public void updateItem(Film film, boolean empty) {
            super.updateItem(film, empty);
            if (empty || film == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(film.getTitle() + " (" + film.getReleaseDate().getYear() +")");
            }
        }
    }

    private void filteredSearch() {
        FilteredList<Film> filteredList = new FilteredList<>(films, x -> true);

        sortInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(film -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return film.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        }));

        SortedList<Film> sortedList = new SortedList<>(filteredList);
        filmList.setItems(sortedList);
    }

    private class loadImage extends Task {
        private Object obj;

        private loadImage(Object obj) {
            this.obj = obj;
        }

        @Override
        protected Image call() {
            try {
                if(obj.getClass() == Film.class) {
                    Film film = (Film) obj;
                    return new Image("https://image.tmdb.org/t/p/w500/" + film.getImageURL(), true);
                } else {
                    TVSeries tvSeries = (TVSeries) obj;
                    return new Image(tvSeries.getPosterURL(), true);
                }
            } catch (Exception e) {
                return new Image("https://mosaikweb.com/wp-content/plugins/lightbox/images/No-image-found.jpg", true);
            }
        }
    }

    private void deleteFilm(Film film) {
        if(main.getFilms().contains(film)) {
            main.getFilms().remove(film);
        }
    }

    @FXML
    private void sortByTitleAscending() {
        Collections.sort(films);
        setSelectedContextMenu(contextSortTitleAsc);
    }

    @FXML
    private void sortByTitleDescending() {
        Collections.sort(films);
        Collections.reverse(films);
        setSelectedContextMenu(contextSortTitleDesc);
    }

    @FXML
    private void sortByReleaseDateAscending() {
        Collections.sort(films, Film.sortByReleaseDate);
        setSelectedContextMenu(contextSortReleaseAsc);
    }

    @FXML
    private void sortByReleaseDateDescending() {
        Collections.sort(films, Film.sortByReleaseDate);
        Collections.reverse(films);
        setSelectedContextMenu(contextSortReleaseDesc);
    }

    @FXML
    private void sortByRuntimeAscending() {
        Collections.sort(films, Film.sortByRuntime);
        setSelectedContextMenu(contextSortRuntimeAsc);
    }

    @FXML
    private void sortByRuntimeDescending() {
        Collections.sort(films, Film.sortByRuntime);
        Collections.reverse(films);
        setSelectedContextMenu(contextSortRuntimeDesc);
    }

    public ListView<Film> getFilmListView() {
        return filmList;
    }
}
