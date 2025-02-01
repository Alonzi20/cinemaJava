package it.unibo.samplejavafx.cinema.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.repositories.BigliettoRepository;
import it.unibo.samplejavafx.cinema.services.biglietto.BigliettoServiceImpl;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.proiezione.ProiezioneService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BigliettoServiceImplTest {
    
    @Mock
    private BigliettoRepository bigliettoRepository;
    @Mock
    private PostoService postoService;
    @Mock
    private ProiezioneService proiezioneService;
    
    private BigliettoServiceImpl bigliettoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bigliettoService = new BigliettoServiceImpl(bigliettoRepository, postoService, proiezioneService);
    }
    
    @Test
    void testImportoBigliettoRidotto() {
        double prezzoRidotto = bigliettoService.importoBiglietto(true);
        assertEquals(5.0, prezzoRidotto); 
    }
    
    @Test
    void testImportoBigliettoIntero() {
        double prezzoIntero = bigliettoService.importoBiglietto(false);
        assertEquals(8.0, prezzoIntero); 
    }

    @Test
    void testCompraBigliettoSuccess() {
        
        Biglietto biglietto = new Biglietto();
        biglietto.setProiezioneId(1L);
        biglietto.setNumero(1L);
        biglietto.setFila("A");
        biglietto.setClienteId(1L);
        
        Proiezione proiezione = new Proiezione();
        proiezione.setSalaId(1L);
        
        when(proiezioneService.findProiezioneById(1L)).thenReturn(proiezione);
        when(proiezioneService.prenota(1L, "A", 1L, 1L, 1L)).thenReturn(1L);
        when(bigliettoRepository.save(any(Biglietto.class))).thenReturn(biglietto);
        
        Biglietto result = bigliettoService.compra(biglietto, false);
        
        assertNotNull(result);
        assertEquals(8.0, result.getPrezzo()); 
        assertFalse(result.isRidotto());
        verify(bigliettoRepository).save(any(Biglietto.class));
    }

    @Test
    void testCompraBigliettoFailure() {
        
        Biglietto biglietto = new Biglietto();
        biglietto.setProiezioneId(1L);
        biglietto.setNumero(1L);
        biglietto.setFila("A");
        biglietto.setClienteId(1L);
        
        Proiezione proiezione = new Proiezione();
        proiezione.setSalaId(1L);
        
        when(proiezioneService.findProiezioneById(1L)).thenReturn(proiezione);
        when(proiezioneService.prenota(1L, "A", 1L, 1L, 1L)).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> bigliettoService.compra(biglietto, false));
    }
}