package no.hiof.oskarsomme.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import no.hiof.oskarsomme.MainJavaFX;
import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;
import no.hiof.oskarsomme.model.windowlauncher.WindowLauncher;

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
    @FXML
    private TabPane tabPane;
    
    private Object currentTarget;
    private ObservableList<Film> films;
    private ObservableList<TVSeries> series;

    @FXML
    private void initialize() {
        // Edit film or tv series attributes.
        btnEdit.setOnAction(event -> {
            new WindowLauncher().goToEditDialog(currentTarget);
            setInfoListing(currentTarget); // Updates info with new values
            // Force refresh on list
            if(currentTarget.getClass() == Film.class)
                filmList.refresh();
            else
                tvSeriesList.refresh();
        });

        // Add new film or tv series to list.
        btnNew.setOnAction(event -> {
            if(tabPane.getSelectionModel().isSelected(0))
                addFilm();
            else if(tabPane.getSelectionModel().isSelected(1))
                addTVSeries();
        });

        // Delete from list.
        btnDelete.setOnAction(event -> deleteFilmOrTVSeries(currentTarget));

        // Handles actions involving context menu over listView.
        contextSortTitleAsc.setOnAction(event -> sortByTitleAscending());
        contextSortTitleDesc.setOnAction(event -> sortByTitleDescending());
        contextSortReleaseAsc.setOnAction(event -> sortByReleaseDateAscending());
        contextSortReleaseDesc.setOnAction(event -> sortByReleaseDateDescending());
        contextSortRuntimeAsc.setOnAction(event -> sortByRuntimeAscending());
        contextSortRuntimeDesc.setOnAction(event -> sortByRuntimeDescending());

        // Double clicking on TVSeries opens new episodelist window.
        tvSeriesList.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                new WindowLauncher().goToEpisodeListing((TVSeries) currentTarget);
                setInfoListing(currentTarget);
            }
        });

        filmList.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                new WindowLauncher().goToEditDialog(currentTarget);
                setInfoListing(currentTarget); // Updates info with new values
                filmList.refresh();
            }
        });

        // Clears the content in the filtering input box when changing tab.
        tabPane.setOnMouseClicked(event -> sortInput.setText(null));
    }


    public void setMain(MainJavaFX main) {
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
        series = main.getTVSeries();
        sortByTitleAscending();
        // Sets info with values of first film in list of films.
        setInfoListing(films.get(0));
        currentTarget = films.get(0);
        // Loads filtered search functionality.
        filteredSearch();
    }

    private void setInfoListing(Object obj) {
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
                if(!tvSeries.isEmpty()) {
                    title.setText(tvSeries.getTitle());
                    date.setText(String.valueOf(tvSeries.getReleaseDate().getDayOfMonth() + " " +
                            MONTH_NAMES[tvSeries.getReleaseDate().getMonthValue() - 1] + " " +
                            tvSeries.getReleaseDate().getYear()));

                    runtimeAndNumberOfSeasonsDescription.setText("Number of episodes:");
                    runtimeAndNumberOfSeasonsLabel.setText(String.valueOf(tvSeries.getNumberOfEpisodes()));
                    description.setText(tvSeries.getDescription());
                    poster.setImage(new loadImage(tvSeries).call());
                }
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
        // Sets cells for Filmlist.
        filmList.setCellFactory(filmListView -> new FilmCell());

        // Adds listener to film listview.
        filmList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        currentTarget = newValue;
                        setInfoListing(newValue);
                    }
                }
        );

        // Sets cells for TVSeries list.
        tvSeriesList.setCellFactory(tvSeriesListView -> new TVSeriesCell());

        // Adds listener to tv series listview.
        tvSeriesList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        currentTarget = newValue;
                        setInfoListing(newValue);
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

    static class TVSeriesCell extends ListCell<TVSeries> {
        @Override
        public void updateItem(TVSeries series, boolean empty) {
            super.updateItem(series, empty);
            if (empty || series == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(series.getTitle());
            }
        }
    }

    private void filteredSearch() {
        FilteredList<Film> filteredListFilm = new FilteredList<>(films, x -> true);
        FilteredList<TVSeries> filteredListTVSeries = new FilteredList<>(series, x -> true);

        sortInput.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredListFilm.setPredicate(film -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return film.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });

            filteredListTVSeries.setPredicate(series -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return series.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        }));

        SortedList<TVSeries> sortedListTVSeries = new SortedList<>(filteredListTVSeries);
        SortedList<Film> sortedListFilm = new SortedList<>(filteredListFilm);
        filmList.setItems(sortedListFilm);
        tvSeriesList.setItems(sortedListTVSeries);
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

                    if(film.getPosterURL().isEmpty()) {
                        return new Image("https://mosaikweb.com/wp-content/plugins/lightbox/images/No-image-found.jpg", true);
                    }

                    if(film.getPosterURL().startsWith("/")) {
                        return new Image("https://image.tmdb.org/t/p/w500" + film.getPosterURL(), true);
                    } else {
                        return new Image(film.getPosterURL(), true);
                    }

                } else {
                    TVSeries tvSeries = (TVSeries) obj;

                    return new Image(tvSeries.getPosterURL(), true);
                }
            } catch (Exception e) {
                return new Image("https://mosaikweb.com/wp-content/plugins/lightbox/images/No-image-found.jpg", true);
            }
        }
    }

    private void deleteFilmOrTVSeries(Object obj) {
        if(obj.getClass() == Film.class) {
            Film film = (Film) obj;
            if(films.contains(film)) {
                films.remove(film);
                MainJavaFX.db.deleteFilm(film);
            }
        } else {
           TVSeries tvSeries = (TVSeries) obj;
           if(series.contains(tvSeries)) {
               series.remove(tvSeries);
               MainJavaFX.db.deleteTVSeries(tvSeries);
           }
        }
    }

    private void addFilm() {
        Film newFilm = new Film();
        new WindowLauncher().goToEditDialog(newFilm);
        if(!newFilm.isEmpty()) {
            films.add(newFilm);
        }
    }

    private void addTVSeries() {
        TVSeries newSeries = new TVSeries();
        new WindowLauncher().goToEditDialog(newSeries);
        if(!newSeries.isEmpty()) {
            series.add(newSeries);
        }
    }

    @FXML
    private void sortByTitleAscending() {
        films.sort(Film.sortByTitle);
        setSelectedContextMenu(contextSortTitleAsc);
    }

    @FXML
    private void sortByTitleDescending() {
        films.sort(Film.sortByTitle.reversed());
        setSelectedContextMenu(contextSortTitleDesc);
    }

    @FXML
    private void sortByReleaseDateAscending() {
        films.sort(Film.sortByReleaseDate);
        setSelectedContextMenu(contextSortReleaseAsc);
    }

    @FXML
    private void sortByReleaseDateDescending() {
        films.sort(Film.sortByReleaseDate.reversed());
        setSelectedContextMenu(contextSortReleaseDesc);
    }

    @FXML
    private void sortByRuntimeAscending() {
        films.sort(Film.sortByRuntime);
        setSelectedContextMenu(contextSortRuntimeAsc);
    }

    @FXML
    private void sortByRuntimeDescending() {
        films.sort(Film.sortByRuntime.reversed());
        setSelectedContextMenu(contextSortRuntimeDesc);
    }
}
