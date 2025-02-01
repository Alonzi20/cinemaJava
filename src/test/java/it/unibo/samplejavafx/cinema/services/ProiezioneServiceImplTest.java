package it.unibo.samplejavafx.cinema.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.PostoRepository;
import it.unibo.samplejavafx.cinema.repositories.ProiezioneRepository;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.proiezione.ProiezioneServiceImpl;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;

class ProiezioneServiceImplTest {
    
    @Mock
    private ProiezioneRepository proiezioneRepository;
    @Mock
    private PostoRepository postoRepository;
    @Mock
    private PostoService postoService;
    @Mock
    private SalaService salaService;
    
    private ProiezioneServiceImpl proiezioneService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proiezioneService = new ProiezioneServiceImpl(proiezioneRepository, postoRepository, 
            postoService, salaService, null, null, null);
    }
    
    @Test
    void testIsPostoPrenotabileTrue() {
        
        long numero = 1L;
        String fila = "A";
        long idProiezione = 1L;
        long idSala = 1L;
        
        Sala sala = new Sala();
        sala.setPosti(100);
        
        when(salaService.findSalaById(idSala)).thenReturn(sala);
        when(postoService.postiPrenotatiByProiezioneId(idProiezione)).thenReturn(50);
        when(postoService.isPostoPrenotabile(numero, fila, idProiezione)).thenReturn(true);
        
        boolean result = proiezioneService.isPostoPrenotabile(numero, fila, idProiezione, idSala);
        
        assertTrue(result);
    }

    @Test
    void testQuantitaPostiLiberi() {
      
        long idProiezione = 1L;
        long idSala = 1L;
        
        Sala sala = new Sala();
        sala.setPosti(100);
        
        when(salaService.findSalaById(idSala)).thenReturn(sala);
        when(postoService.postiPrenotatiByProiezioneId(idProiezione)).thenReturn(30);
        
        int result = proiezioneService.quantitaPostiLiberi(idProiezione, idSala);
        
        assertEquals(70, result);
    }
}
