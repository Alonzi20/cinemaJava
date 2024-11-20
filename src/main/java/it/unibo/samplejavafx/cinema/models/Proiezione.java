package it.unibo.samplejavafx.cinema.models;

public class Proiezione {
    private Film film;
    private String sala;
    private String orario;
    private int postiDisponibili;

    public Proiezione(Film film, String sala, String orario, int postiDisponibili) {
        this.film = film;
        this.sala = sala;
        this.orario = orario;
        this.postiDisponibili = postiDisponibili;
    }
}
