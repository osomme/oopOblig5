package no.hiof.oskarsomme.datamanagement;

import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.Episode;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;
import no.hiof.oskarsomme.model.windowlauncher.WindowLauncher;

import java.sql.*;
import java.time.LocalDate;

public class MediaDatabaseManager {
    private Connection connection;
    private Statement statement;

    public MediaDatabaseManager(Connection connection) {
        this.connection = connection;

        try {
            statement = connection.createStatement();

            // Enables foreign key enforcement in SQLite.
            statement.execute("PRAGMA FOREIGN_KEYS = ON");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadFilms() {
        try {
            ResultSet queryResult = statement.executeQuery("SELECT * FROM film;");

            while(queryResult.next()) {
                new Film(queryResult.getString("title"),
                        queryResult.getString("description"),
                        queryResult.getInt("runningtime"),
                        LocalDate.parse(queryResult.getString("releasedate")),
                        queryResult.getString("poster_url"),
                        queryResult.getInt("filmid"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadTVSeries() {
        try {
            ResultSet TVSeriesQuery = statement.executeQuery("SELECT * FROM tvseries;");

            while(TVSeriesQuery.next()) {
                new TVSeries(TVSeriesQuery.getString("title"),
                        TVSeriesQuery.getString("description"),
                        LocalDate.parse(TVSeriesQuery.getString("releasedate")),
                        TVSeriesQuery.getString("poster_url"),
                        TVSeriesQuery.getInt("tvseriesid"));
            }

            for(TVSeries series : TVSeries.listOfTVseries) {
                loadEpisodes(series);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadEpisodes(TVSeries series) throws SQLException {
        ResultSet episodeQuery = statement.executeQuery(
                "SELECT * FROM episode WHERE seriesid = " + series.getId() + ";");

        while(episodeQuery.next()) {
            Episode episode = new Episode(episodeQuery.getInt("episode"),
                    episodeQuery.getInt("season"),
                    episodeQuery.getString("title"),
                    LocalDate.parse(episodeQuery.getString("releasedate")),
                    episodeQuery.getInt("runtime"),
                    episodeQuery.getInt("episodeid"));

            series.addEpisode(episode);
        }

    }


    public void updateFilm(Film film) {
        try {
            String query = String.format("UPDATE film SET title = '%s', description = '%s', " +
                            "poster_url = '%s', runningtime = %d, releasedate = '%s'" +
                            "WHERE filmid = %d;", film.getTitle(), film.getDescription(), film.getPosterURL(), film.getRunningTime(),
                    film.getReleaseDate().toString(), film.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateTVSeries(TVSeries series) {
        try {
            String query = String.format("UPDATE tvseries SET title = '%s', description = '%s', " +
                            "poster_url = '%s', releasedate = '%s' WHERE tvseriesid = %d;", series.getTitle(),
                    series.getDescription(), series.getPosterURL(), series.getReleaseDate().toString(), series.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateEpisode(Episode episode) {
        try {
            String query = String.format("UPDATE episode SET title = '%s', season = %d, episode = %d, " +
                    "runtime = %d, releasedate = '%s' WHERE episodeid = %d;", episode.getTitle(), episode.getSeason(),
                    episode.getEpisodeNumber(), episode.getRunningTime(), episode.getReleaseDate().toString(),
                    episode.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addFilm(Film film) {
        try {
            String query = String.format("INSERT INTO film (title, description, poster_url, runningtime, releasedate)" +
                            "VALUES('%s', '%s', '%s', %d, '%s');", film.getTitle(),
                    film.getDescription(), film.getPosterURL(), film.getRunningTime(), film.getReleaseDate().toString());

            statement.executeUpdate(query);

            /* Gives the film object an id that corresponds with database id,
            so that an update/deletion is possible before the program is reloaded. */
            updateFilmIdObject(film);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addTVSeries(TVSeries series) {
        try {
            String query = String.format("INSERT INTO tvseries (title, description, releasedate, poster_url)" +
                            "VALUES('%s', '%s', '%s', '%s');", series.getTitle(), series.getDescription(),
                    series.getReleaseDate().toString(), series.getPosterURL());

            statement.executeUpdate(query);

            updateTVSeriesObject(series);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean addEpisode(Episode episode, TVSeries series) {
        try {
            String query = String.format("INSERT INTO episode (title, season, episode, runtime, releasedate, seriesid) " +
                    "VALUES('%s', %d, %d, %d, '%s', %d);", episode.getTitle(), episode.getSeason(), episode.getEpisodeNumber(),
                    episode.getRunningTime(), episode.getReleaseDate().toString(), series.getId());


            statement.executeUpdate(query);
            updateEpisodeObject(episode, series);

            return true;
        } catch (SQLException e) {
            // Catches exception if episode already exists and user attempts to add it again.
            if(e.getErrorCode() == 19) {
                WindowLauncher.episodeAlreadyExistsErrorBox();
            }
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void updateFilmIdObject(Film film) throws SQLException {
        String query = String.format("SELECT filmid FROM film WHERE title = '%s' " +
                "AND description = '%s' " +
                "AND runningtime = %d " +
                "AND releasedate = '%s';", film.getTitle(), film.getDescription(), film.getRunningTime(), film.getReleaseDate().toString());

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            film.setId(resultSet.getInt("filmid"));
        }
    }

    private void updateTVSeriesObject(TVSeries series) throws SQLException {
        String query = String.format("SELECT tvseriesid FROM tvseries WHERE title = '%s';", series.getTitle());

        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()) {
            series.setId(resultSet.getInt("tvseriesid"));
        }
    }

    private void updateEpisodeObject(Episode episode, TVSeries series) throws SQLException {
        String query = String.format("SELECT episodeid FROM episode WHERE seriesid = %d AND season = %d AND episode = %d;",
                series.getId(), episode.getSeason(), episode.getEpisodeNumber());

        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()) {
            episode.setId(resultSet.getInt("episodeid"));
        }
    }

    public void deleteFilm(Film film) {
        try {
            String query = String.format("DELETE FROM film WHERE filmid = %d;", film.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteTVSeries(TVSeries series) {
        try {
            String query = String.format("DELETE FROM tvseries WHERE tvseriesid = %d;", series.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteEpisode(Episode episode) {
        try {
            String query = String.format("DELETE FROM episode WHERE episodeid = %d;", episode.getId());

            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean contains(Object object) {
        try {
            int i = 0;
            ResultSet resultSet;

            if(object.getClass() == Film.class) {
                Film film = (Film) object;
                String query = String.format("SELECT * FROM film WHERE filmid = %d;", film.getId());

                resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    i++;
                }
            } else if(object.getClass() == TVSeries.class) {
                TVSeries series = (TVSeries) object;
                String query = String.format("SELECT * FROM tvseries WHERE tvseriesid = %d;", series.getId());

                resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    i++;
                }
            } else {
                Episode episode = (Episode) object;

                String query = String.format("SELECT * FROM episode WHERE episodeid = %d;", episode.getId());

                resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    i++;
                }
            }

            return i > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
