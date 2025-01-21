package it.unibo.samplejavafx;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import it.unibo.samplejavafx.cinema.services.orari_proiezioni.OrariProiezioniService;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleManager {
    private static ScheduleManager instance;
    private final Map<Integer, Map<String, List<String>>> scheduleCache = new HashMap<>();
    private final List<String> DAYS = Arrays.asList("LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ", "GIOVEDÌ", "VENERDÌ", "SABATO", "DOMENICA");
    
    // Reference al service
    private final OrariProiezioniService orariProiezioniService;
    
    private ScheduleManager(OrariProiezioniService orariProiezioniService) {
        this.orariProiezioniService = orariProiezioniService;
    }
    
    public static ScheduleManager getInstance(OrariProiezioniService orariProiezioniService) {
        if (instance == null) {
            instance = new ScheduleManager(orariProiezioniService);
        }
        return instance;
    }
    
    public Map<String, List<String>> getScheduleForMovie(Film movie, List<Film> weeklyMovies) {
        int movieIndex = weeklyMovies.indexOf(movie);
        return scheduleCache.computeIfAbsent(movieIndex, this::generateScheduleForMovie);
    }

    // TODO Andrea o Francesco: [20/01/2025] Quando i film saranno nel DB:
    // 1. Implementare metodo generateProjections(Film film) che:
    //    - Per ogni giorno (LocalDate.now() fino a +7 giorni):
    //       - Converte LocalDate in java.sql.Date per il DB
    //       - Seleziona orari dalla tabella orari_proiezioni in base al pattern del giorno
    //       - Per ogni orario crea una Proiezione con:
    //          * film_id del film
    //          * sala_id disponibile
    //          * data del giorno corrente (java.sql.Date)
    //          * orario_proiezione_id dell'orario selezionato
    //    - Considera la durata del film per evitare sovrapposizioni nella stessa sala
    //    - Salva le proiezioni generate nel DB
    // 2. Aggiornare l'UI per utilizzare le proiezioni dal database invece degli orari generati

    private List<String> selectRandomTimes(List<OrariProiezioni> orari, Random random, int maxTimes) {
        if (orari.isEmpty()) return new ArrayList<>();
        
        List<OrariProiezioni> shuffled = new ArrayList<>(orari);
        Collections.shuffle(shuffled, random);
        return shuffled.stream()
            .limit(maxTimes)
            .map(o -> o.getStartTime().toString().substring(0, 5))
            .sorted()
            .collect(Collectors.toList());
    }
    
    private Map<String, List<String>> generateScheduleForMovie(int movieIndex) {
        Map<String, List<String>> schedule = new HashMap<>();
        Random movieRandom = new Random(movieIndex * 31L);
        
        for (String day : DAYS) {
            List<String> times = new ArrayList<>();
            
            if (day.equals("SABATO") || day.equals("DOMENICA")) {
                if (movieRandom.nextDouble() < 0.4) {
                    List<OrariProiezioni> morningPattern = orariProiezioniService.findOrariByPatternType("WEEKEND_MORNING");
                    times.addAll(selectRandomTimes(morningPattern, movieRandom, 1));
                }
                
                List<OrariProiezioni> mainPattern = orariProiezioniService.findOrariByPatternType("WEEKEND");
                times.addAll(selectRandomTimes(mainPattern, movieRandom, 5)); 
            } 
            else if (day.equals("VENERDÌ")) {
                List<OrariProiezioni> pattern = orariProiezioniService.findOrariByPatternType("WEEKDAY");
                times.addAll(selectRandomTimes(pattern, movieRandom, 3));
                if (movieRandom.nextDouble() < 0.3) {
                    times.add("00:15"); 
                }
            } 
            else {
                List<OrariProiezioni> pattern = orariProiezioniService.findOrariByPatternType("WEEKDAY");
                times.addAll(selectRandomTimes(pattern, movieRandom, 3));
            }
            
            Collections.sort(times);
            schedule.put(day, times);
        }
        
        return schedule;
    }
    
    public List<String> getDays() {
        return DAYS;
    }
}