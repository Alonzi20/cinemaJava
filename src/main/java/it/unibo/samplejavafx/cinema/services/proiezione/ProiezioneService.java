package it.unibo.samplejavafx.cinema.services.proiezione;

public interface ProiezioneService {
    public int postiLiberi(long idProiezione, long idSala);

    public boolean isPrenotabile(long idProiezione, long idSala);

    public boolean prenota(long idPosto, long idProiezione, long idSala);
}
