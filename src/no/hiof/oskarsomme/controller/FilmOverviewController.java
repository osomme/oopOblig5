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
            contextSortReleaseDesc, contextSortRuntimeAsc, contextSortRuntimeDesc, contextSortTitleAscTV,
            contextSortTitleDescTV, contextSortDateAscTV, contextSortDateDescTV, contextSortEpsAscTV,
            contextSortEpsDescTV;
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

    private ToggleGroup contextGroupFilm, contextGroupTVSeries;
    private Object currentTarget;
    private ObservableList<Film> films;
    private ObservableList<TVSeries> series;

    @FXML
    private void initialize() {
        setContextMenu();

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
        //sortByTitleAscending();
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

    private void setSelectedContextMenus() {
        final RadioMenuItem[] RADIO_MENU_ITEMS_FILM = {contextSortTitleAsc, contextSortTitleDesc, contextSortReleaseAsc,
                contextSortReleaseDesc, contextSortRuntimeAsc, contextSortRuntimeDesc};

        final RadioMenuItem[] RADIO_MENU_ITEMS_TVSERIES = {contextSortDateAscTV, contextSortDateDescTV, contextSortEpsAscTV,
                contextSortEpsDescTV, contextSortTitleAscTV, contextSortTitleDescTV};

        for(RadioMenuItem menuItem : RADIO_MENU_ITEMS_FILM) {
            menuItem.setToggleGroup(contextGroupFilm);
        }

        for(RadioMenuItem menuItem : RADIO_MENU_ITEMS_TVSERIES) {
            menuItem.setToggleGroup(contextGroupTVSeries);
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

    private void setContextMenu() {
        contextGroupFilm = new ToggleGroup();
        contextGroupTVSeries = new ToggleGroup();
        setSelectedContextMenus();

        // Handles actions involving context menu over film listview.
        contextSortTitleAsc.setOnAction(event -> films.sort(Film.sortByTitle));
        contextSortTitleDesc.setOnAction(event -> films.sort(Film.sortByTitle.reversed()));
        contextSortReleaseAsc.setOnAction(event -> films.sort(Film.sortByReleaseDate));
        contextSortReleaseDesc.setOnAction(event -> films.sort(Film.sortByReleaseDate.reversed()));
        contextSortRuntimeAsc.setOnAction(event -> films.sort(Film.sortByRuntime));
        contextSortRuntimeDesc.setOnAction(event -> films.sort(Film.sortByRuntime.reversed()));

        // Handles actions involving context menu over tv series listview.
        contextSortTitleAscTV.setOnAction(event -> series.sort(TVSeries.sortByTitle));
        contextSortTitleDescTV.setOnAction(event -> series.sort(TVSeries.sortByTitle.reversed()));
        contextSortDateAscTV.setOnAction(event -> series.sort(TVSeries.sortByReleaseDate));
        contextSortDateDescTV.setOnAction(event -> series.sort(TVSeries.sortByReleaseDate.reversed()));
        contextSortEpsAscTV.setOnAction(event -> series.sort(TVSeries.sortByNumberOfEpisodes));
        contextSortEpsDescTV.setOnAction(event -> series.sort(TVSeries.sortByNumberOfEpisodes.reversed()));
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
}
