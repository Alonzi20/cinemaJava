package it.unibo.samplejavafx.cinema.models;

public class Film {
    private String titolo;
    private String genere;
    private String regista;
    private int anno;

    public Film(String titolo, String genere, String regista, int anno) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
    }
}
