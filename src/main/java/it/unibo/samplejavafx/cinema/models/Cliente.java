package it.unibo.samplejavafx.cinema.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Cliente {
  int id;
  String nome;
  String cognome;
  String email;
}
