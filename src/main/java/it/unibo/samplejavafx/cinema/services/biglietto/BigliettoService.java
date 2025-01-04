package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;

public interface BigliettoService {
    Biglietto compra(long idProiezione, long idSala, long idPosto, boolean ridotto);
}
