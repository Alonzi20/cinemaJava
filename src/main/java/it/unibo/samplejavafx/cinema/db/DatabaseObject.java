package it.unibo.samplejavafx.cinema.db;

import it.unibo.samplejavafx.cinema.utils.SearchableObject;
import it.unibo.samplejavafx.cinema.utils.StringUtil;
import it.unibo.samplejavafx.cinema.utils.Validator;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe base da cui deve derivare ogni oggetto che richiede il salvataggio, come tabella, su
 * database. Prevede metodi di aiuto nella gestione dei campi flag.
 */
@Slf4j
public abstract class DatabaseObject extends SearchableObject {

  /** Costruttore di default richiesto da ORMLite. */
  public DatabaseObject() {}

  /**
   * Ritorna il valore di un campo di questa classe per mezzo delle reflection.
   *
   * @param fieldName Campo di cui si desidera ottenere il valore.
   * @return Valore del campo specificato.
   */
  public String getFieldValue(String fieldName) {
    String stringValue = "";

    try {
      Object fieldValue = this.getClass().getDeclaredField(fieldName).get(this);
      if (fieldValue instanceof String) stringValue = (String) fieldValue;
      else if (fieldValue instanceof Integer) stringValue = fieldValue.toString();

    } catch (Exception e) {
      log.error("Errore durante la lettura del campo {} dell'oggetto {}", fieldName, this);
      e.printStackTrace();
    }

    return stringValue;
  }

  /**
   * Legge un dato dal campo flag.
   *
   * @param flags Campo flag da leggere.
   * @param position Posizione in cui iniziare a leggere il valore.
   * @return Ritorna il valore letto come boolean.
   */
  protected boolean getFlagsValueAsBoolean(String flags, int position) {
    try {
      return (getFlagsValueAsString(flags, position, 1).equals("S"));
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Legge un dato dal campo flag.
   *
   * @param flags Campo flag da leggere.
   * @param position Posizione in cui iniziare a leggere il valore.
   * @return Ritorna il valore letto come char.
   */
  protected char getFlagsValueAsChar(String flags, int position) {
    return getFlagsValueAsString(flags, position, 1).charAt(0);
  }

  /**
   * Legge un dato dal campo flag.
   *
   * @param flags Campo flag da leggere.
   * @param position Posizione in cui iniziare a leggere il valore.
   * @param length Lunghezza, espressa in caratteri, del valore da leggere.
   * @return Ritorna il valore letto come Integer.
   */
  protected Integer getFlagsValueAsInt(String flags, int position, int length) {
    String val = getFlagsValueAsString(flags, position, length).trim();
    if (Validator.isStringNullOrEmpty(val)) val = "0";

    return Integer.parseInt(val);
  }

  /**
   * Legge un dato dal campo flag.
   *
   * @param flags Campo flag da leggere.
   * @param position Posizione in cui iniziare a leggere il valore.
   * @param length Lunghezza, espressa in caratteri, del valore da leggere.
   * @return Ritorna il valore letto come String.
   */
  protected String getFlagsValueAsString(String flags, int position, int length) {
    try {
      return flags.substring(position, position + length);
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Se questo oggetto deve prevedere una forma di sincronizzazione, specificare qui come si
   * chiamerà il file xml da utilizzare per lo scambio dati.
   *
   * @return Ritorna il nome del file xml senza estensione che si userà per lo scambio dati.
   */
  public String getXmlFileNameWithoutExtension() {
    return "";
  }

  /**
   * Aggiunge un valore al campo flag.
   *
   * @param flags Campo flag da aggiornare.
   * @param position Posizione in cui inserire il valore.
   * @param value Valore da inserire.
   * @return Ritorna il nuovo valore da assegnare al campo flag.
   */
  protected String setFlagsValue(String flags, int position, boolean value) {
    return setFlagsValue(flags, position, 1, (value) ? "S" : "N");
  }

  /**
   * Aggiunge un valore al campo flag.
   *
   * @param flags Campo flag da aggiornare.
   * @param position Posizione in cui inserire il valore.
   * @param value Valore da inserire.
   * @return Ritorna il nuovo valore da assegnare al campo flag.
   */
  protected String setFlagsValue(String flags, int position, int value) {
    String numero = Integer.toString(value);
    return setFlagsValue(flags, position, numero.length(), numero);
  }

  /**
   * Aggiunge un valore al campo flag.
   *
   * @param flags Campo flag da aggiornare.
   * @param position Posizione in cui inserire il valore.
   * @param length Lunghezza, espressa in caratteri, del valore da inserire.
   * @param value Valore da inserire.
   * @return Ritorna il nuovo valore da assegnare al campo flag.
   */
  protected String setFlagsValue(String flags, int position, int length, int value) {
    String numero = Integer.toString(value);
    return setFlagsValue(flags, position, length, numero);
  }

  /**
   * Aggiunge un valore al campo flag.
   *
   * @param flags Campo flag da aggiornare.
   * @param position Posizione in cui inserire il valore.
   * @param length Lunghezza, espressa in caratteri, del valore da inserire.
   * @param value Valore da inserire.
   * @return Ritorna il nuovo valore da assegnare al campo flag.
   */
  protected String setFlagsValue(String flags, int position, int length, String value) {
    String tempString = StringUtil.padRight(flags, position + length);
    flags =
        tempString.substring(0, position)
            + StringUtil.padRight(value, length).substring(0, length)
            + (tempString.length() > (position + length)
                ? tempString.substring(position + length)
                : "");

    return flags;
  }

  public String getTableName() {
    return "";
  }
}
