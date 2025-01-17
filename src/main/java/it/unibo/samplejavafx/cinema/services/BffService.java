package it.unibo.samplejavafx.cinema.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BffService {
  private static final String BASE_URL =
      "http://localhost:8080"; // Modifica con l'URL corretto del tuo backend
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  // --- BIGLIETTO ENDPOINTS ---

  public Biglietto findBiglietto(long id) throws Exception {
    String url = BASE_URL + "/biglietto?id=" + id;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Biglietto.class);
    } else {
      throw new RuntimeException(
          "Errore durante il recupero del biglietto: " + response.statusCode());
    }
  }

  public List<Biglietto> findAllBiglietti() throws Exception {
    String url = BASE_URL + "/biglietto/all";
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero di tutti i biglietti: " + response.statusCode());
    }
  }

  public List<Biglietto> findAllBigliettiByClienteId(long idCliente) throws Exception {
    String url = BASE_URL + "/biglietto/all/" + idCliente;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero dei biglietti per cliente: " + response.statusCode());
    }
  }

  public Double importoBiglietto(boolean ridotto) throws Exception {
    String url = BASE_URL + "/biglietto/importo?ridotto=" + ridotto;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return Double.parseDouble(response.body());
    } else {
      throw new RuntimeException(
          "Errore durante il calcolo dell'importo del biglietto: " + response.statusCode());
    }
  }

  public Biglietto compraBiglietto(long idProiezione, long idPosto, boolean ridotto)
      throws Exception {
    String url =
        BASE_URL
            + "/biglietto/compra?idProiezione="
            + idProiezione
            + "&idPosto="
            + idPosto
            + "&ridotto="
            + ridotto;
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Biglietto.class);
    } else {
      throw new RuntimeException(
          "Errore durante l'acquisto del biglietto: " + response.statusCode());
    }
  }

  // --- FILM ENDPOINTS ---

  public Film findByFilmId(long id) throws Exception{
    String url = BASE_URL + "/film?id=" + id;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Film.class);
    } else {
      throw new RuntimeException(
          "Errore durante il recupero del film: " + response.statusCode());
    }
  }

  // --- SALA ENDPOINTS ---

  public Sala findBySalaId(long id) throws Exception{
    String url = BASE_URL + "/sala?id=" + id;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Sala.class);
    } else {
      throw new RuntimeException(
          "Errore durante il recupero della sala: " + response.statusCode());
    }
  }

  // --- CLIENTE ENDPOINTS ---

  public Cliente findCliente(long id) throws Exception {
    String url = BASE_URL + "/cliente?id=" + id;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Cliente.class);
    } else {
      throw new RuntimeException(
          "Errore durante il recupero del cliente: " + response.statusCode());
    }
  }

  public List<Cliente> findAllClienti() throws Exception {
    String url = BASE_URL + "/cliente/all";
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero di tutti i clienti: " + response.statusCode());
    }
  }

  public Cliente createCliente(String nome, String cognome, String email) throws Exception {
    String url = BASE_URL + "/cliente?nome=" + nome + "&cognome=" + cognome + "&email=" + email;
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Cliente.class);
    } else {
      throw new RuntimeException(
          "Errore durante la creazione del cliente: " + response.statusCode());
    }
  }

  // --- PROIEZIONE ENDPOINTS ---

  public Proiezione findProiezione(long id) throws Exception {
    String url = BASE_URL + "/proiezione?id=" + id;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), Proiezione.class);
    } else {
      throw new RuntimeException(
          "Errore durante il recupero della proiezione: " + response.statusCode());
    }
  }

  public List<Proiezione> findAllProiezioni() throws Exception {
    String url = BASE_URL + "/proiezione/all";
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero di tutte le proiezioni: " + response.statusCode());
    }
  }

  // Da usare per aggiungere gli orari delle proiezioni nei dettagli di ciascun film
  // Poi da usare per comprare un biglietto scegliendo la proiezione sulla base dell'orario
  public List<Proiezione> findAllProiezioniByFilmId(long idFilm) throws Exception {
    String url = BASE_URL + "/proiezione/all" + idFilm;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero delle proiezioni per film: " + response.statusCode());
    }
  }

  public boolean isSalaPrenotabile(long idProiezione, long idSala) throws Exception {
    String url =
        BASE_URL + "/proiezione/prenotabile?idProiezione=" + idProiezione + "&idSala=" + idSala;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return Boolean.parseBoolean(response.body());
    } else {
      throw new RuntimeException(
          "Errore durante la verifica della sala prenotabile: " + response.statusCode());
    }
  }

  public Map<String, Long> postiLiberi(long idProiezione, long idSala) throws Exception {
    String url =
        BASE_URL + "/proiezione/postiLiberi?idProiezione=" + idProiezione + "&idSala=" + idSala;
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 200) {
      return objectMapper.readValue(response.body(), new TypeReference<>() {});
    } else {
      throw new RuntimeException(
          "Errore durante il recupero dei posti liberi: " + response.statusCode());
    }
  }
}
