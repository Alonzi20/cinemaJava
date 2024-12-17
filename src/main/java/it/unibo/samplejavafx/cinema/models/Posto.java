package it.unibo.samplejavafx.cinema.models;

import lombok.Data;

@Data
public class Posto {
  int numero;
  String fila;
  Sala sala;
}
