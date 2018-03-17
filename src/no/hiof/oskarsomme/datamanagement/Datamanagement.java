package no.hiof.oskarsomme.datamanagement;

import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import no.hiof.oskarsomme.model.media.film.Film;

import java.io.*;

public class Datamanagement {

    public static void loadFromJson(String filePath) {
        Gson gson = new Gson();
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String jsonString = reader.readLine();
            gson.fromJson(jsonString, Film[].class);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Data could not be loaded");
            alert.showAndWait();
        }
    }

    public static void saveToJson(String filePath, ObservableList<Film> list) {
        Gson gson = new Gson();
        File outputFile = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            String filmToJson = gson.toJson(list);
            writer.write(filmToJson);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Data could not be saved");
            alert.showAndWait();
        }
    }
}
