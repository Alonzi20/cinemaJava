package it.unibo.samplejavafx.cinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unibo.samplejavafx.cinema.application.models.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {}
