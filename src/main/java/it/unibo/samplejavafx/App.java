package it.unibo.samplejavafx;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class App {
    private static final String API_KEY = "2ad42fcfac14ac8869896349fa9c4b6f";

    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();

        int movieId = 550; 
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language=it-IT";

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);

            String title = jsonObject.getString("title");
            String overview = jsonObject.getString("overview");

            System.out.println("Titolo: " + title);
            System.out.println("Descrizione: " + overview);
        } else {
            System.err.println("Errore: " + response.body().string());
        }
    }
}
