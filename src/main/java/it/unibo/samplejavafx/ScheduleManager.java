package it.unibo.samplejavafx;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.samplejavafx.cinema.models.Film;

import java.util.*;

public class ScheduleManager {
    private static ScheduleManager instance;
    private final Map<Integer, Map<String, List<String>>> scheduleCache = new HashMap<>();
    private ScheduleData scheduleData;
    
    public static class ScheduleData {
        public List<String> DAYS;
        public List<List<String>> WEEKDAY_PATTERNS;
        public List<List<String>> WEEKEND_PATTERNS;
        public List<List<String>> WEEKEND_MORNING_PATTERNS;
    }
    
    private ScheduleManager() {
        loadScheduleData();
    }
    
    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }
    
    private void loadScheduleData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            scheduleData = mapper.readValue(
                getClass().getResourceAsStream("/schedulePatterns.json"), 
                ScheduleData.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il caricamento dei pattern di programmazione: " + e.getMessage(), e);
        }
    }
    
    public Map<String, List<String>> getScheduleForMovie(Film movie, List<Film> weeklyMovies) {
        int movieIndex = weeklyMovies.indexOf(movie);
        return scheduleCache.computeIfAbsent(movieIndex, this::generateScheduleForMovie);
    }
    
    private Map<String, List<String>> generateScheduleForMovie(int movieIndex) {
        Map<String, List<String>> schedule = new HashMap<>();
        Random movieRandom = new Random(movieIndex * 31L);
        
        for (int i = 0; i < scheduleData.DAYS.size(); i++) {
            String day = scheduleData.DAYS.get(i);
            List<String> times = new ArrayList<>();
            
            if (day.equals("SABATO") || day.equals("DOMENICA")) {
                if (movieRandom.nextDouble() < 0.4) {
                    List<String> morningPattern = scheduleData.WEEKEND_MORNING_PATTERNS.get(
                        movieRandom.nextInt(scheduleData.WEEKEND_MORNING_PATTERNS.size())
                    );
                    times.addAll(morningPattern);
                }
                
                List<String> mainPattern = scheduleData.WEEKEND_PATTERNS.get(
                    movieRandom.nextInt(scheduleData.WEEKEND_PATTERNS.size())
                );
                times.addAll(mainPattern);
            } else if (day.equals("VENERDÌ")) {
                List<String> pattern = scheduleData.WEEKDAY_PATTERNS.get(
                    movieRandom.nextInt(scheduleData.WEEKDAY_PATTERNS.size())
                );
                times.addAll(pattern);
                if (movieRandom.nextDouble() < 0.3) {
                    times.add("00:15");
                }
            } else {
                List<String> pattern = scheduleData.WEEKDAY_PATTERNS.get(
                    movieRandom.nextInt(scheduleData.WEEKDAY_PATTERNS.size())
                );
                times.addAll(pattern);
            }
            
            Collections.sort(times);
            schedule.put(day, times);
        }
        
        return schedule;
    }
    
    public List<String> getDays() {
        return scheduleData.DAYS;
    }
}