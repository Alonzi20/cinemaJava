package it.unibo.samplejavafx.cinema.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.unibo.samplejavafx.cinema.repositories.PostoRepository;
import it.unibo.samplejavafx.cinema.services.posto.PostoServiceImpl;

class PostoServiceImplTest {
    
    @Mock
    private PostoRepository postoRepository;
    
    private PostoServiceImpl postoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postoService = new PostoServiceImpl(postoRepository);
    }
    
    @Test
    void testIsPostoPrenotabileTrue() {
      
        long numero = 1L;
        String fila = "A";
        long idProiezione = 1L;
        
        when(postoRepository.findAllByNumeroAndFilaAndPrenotatoAndProiezione_Id(
            numero, fila, true, idProiezione)).thenReturn(Collections.emptyList());
        
        boolean result = postoService.isPostoPrenotabile(numero, fila, idProiezione);
        
        assertTrue(result);
    }

    @Test
    void testPostiPrenotati() {
       
        long idProiezione = 1L;
        when(postoRepository.countByPrenotatoAndProiezione_Id(true, idProiezione)).thenReturn(5);
        
        int result = postoService.postiPrenotatiByProiezioneId(idProiezione);
        
        assertEquals(5, result);
    }
}
