package it.unibo.samplejavafx.cinema.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.services.MovieProjections;

public class MovieChangesetGenerator {
    private static final String CHANGESET_TEMPLATE = """
            <?xml version="1.0" encoding="UTF-8"?>
            <databaseChangeLog
                    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                                    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.0.xsd">
                
                <changeSet id="movies_update_%d" author="auto_generator">
                    <!-- Clear existing films -->
                    <sql>DELETE FROM Film</sql>
                    
                    %s
                </changeSet>
                
            </databaseChangeLog>
            """;

    private static final String INSERT_TEMPLATE = """
            <insert tableName="Film">
                <column name="title" value="%s"/>
                <column name="overview" value="%s"/>
                <column name="release_date" value="%s"/>
                <column name="poster_path" value="%s"/>
                <column name="genres" value="%s"/>
                <column name="duration" value="%d"/>
                <column name="cast" value="%s"/>
                <column name="director" value="%s"/>
                <column name="adult" value="%s"/>
            </insert>
            """;

    public static void generateChangeset() {
        try {
            //crea percorso dir
            String projectDir = new File("").getAbsolutePath();
            
            //creazione percorso completo per il file e le directory
            File changelogDir = new File(projectDir, "src/main/resources/db/changelog/changes");
            changelogDir.mkdirs(); 
            
            File outputFile = new File(changelogDir, "movies-update.xml");
            
            MovieProjections movieService = new MovieProjections();
            List<Film> weeklyMovies = movieService.getWeeklyMovies();
            
            StringBuilder insertsBuilder = new StringBuilder();
            
            for (Film film : weeklyMovies) {
                String insertStatement = String.format(INSERT_TEMPLATE,
                    escapeXml(film.getTitle()),
                    escapeXml(film.getOverview()),
                    escapeXml(film.getReleaseDate()),
                    escapeXml(film.getPosterPath()),
                    escapeXml(film.getGenres()),
                    film.getDuration(),
                    escapeXml(film.getCast()),
                    escapeXml(film.getDirector()),
                    film.isAdult()
                );
                insertsBuilder.append(insertStatement);
            }
            
            
            long changesetId = System.currentTimeMillis();
            
            String completeChangeset = String.format(CHANGESET_TEMPLATE, 
                changesetId,
                insertsBuilder.toString()
            );
            
            
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                writer.write(completeChangeset);
                System.out.println("Changeset generato con successo in: " + outputFile.getAbsolutePath());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate changeset file", e);
        }
    }
    
    private static String escapeXml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;")
                   .replace("\n", " ")  
                   .replace("\r", "")   
                   .trim();             
    }

    public static void main(String[] args) {
        generateChangeset();
    }
}