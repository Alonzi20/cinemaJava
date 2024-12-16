package it.unibo.samplejavafx.cinema.services;

import it.unibo.samplejavafx.cinema.models.Film;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MovieProjections {
    private static final String API_KEY = "2ad42fcfac14ac8869896349fa9c4b6f";
    private List<Film> weeklyMovies;

    public MovieProjections() {
        this.weeklyMovies = new ArrayList<>();
        fetchWeeklyMovies();
    }

    private void fetchWeeklyMovies() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=it-IT&page=1&region=IT")
            .get()
            .addHeader("accept", "application/json")
            .build();
    
        try {
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray results = jsonObject.getJSONArray("results");
    
                
                List<Film> allMovies = new ArrayList<>();
    
                //conversione film
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieJson = results.getJSONObject(i);
                    int movieId = movieJson.getInt("id");
                    
                    //dettagli specifici film aggiuntivi non in now playing
                    Film film = getMovieDetails(movieId);
                    
                    if (film != null) {
                        allMovies.add(film);
                    }
                }
    
                //ordinamento film per data più recente
                allMovies.sort((m1, m2) -> {
                    LocalDate date1 = LocalDate.parse(m1.getReleaseDate());
                    LocalDate date2 = LocalDate.parse(m2.getReleaseDate());
                    return date2.compareTo(date1);
                });
    
                weeklyMovies.clear();
                weeklyMovies.addAll(allMovies);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Recupero dettagli specifici di un film
    private Film getMovieDetails(int movieId) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language=it-IT")
            .get()
            .addHeader("accept", "application/json")
            .build();
    
        try {
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject movieDetails = new JSONObject(jsonData);
    
                //estrazione generi film
                JSONArray genresArray = movieDetails.getJSONArray("genres");
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genresArray.length(); i++) {
                    genres.add(genresArray.getJSONObject(i).getString("name"));
                }
    
                return new Film(
                    movieDetails.getInt("id"),
                    movieDetails.getString("title"),
                    movieDetails.getString("overview"),
                    movieDetails.getString("release_date"),
                    movieDetails.getString("poster_path"),
                    genres,  
                    movieDetails.getInt("runtime"),
                    movieDetails.getBoolean("adult")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    //recupero film settimanali
    public List<Film> getWeeklyMovies() {
        return new ArrayList<>(weeklyMovies);
    }

    //metodo di ricerca film (da cambiare)
    public List<Film> searchMovieByName(String name) {
        List<Film> foundMovies = new ArrayList<>();
        
        for (Film film : weeklyMovies) {
            if (film.getTitle().toLowerCase().contains(name.toLowerCase())) {
                foundMovies.add(film);
            }
        }
        
        return foundMovies;
    }
}