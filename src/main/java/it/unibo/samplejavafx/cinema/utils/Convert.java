package it.unibo.samplejavafx.cinema.utils;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** Classe dove sono definiti dei metodi per convertire diversi tipi di dati. */
public final class Convert {

  /**
   * Converte una stringa in un oggetto Date.
   *
   * @param value stringa con la data da convertire.
   * @return Ritorna un oggetto Date rappresentante la data passata come parametro. Ritorna null in
   *     caso di errore.
   */
  public static Date toDate(final String value) {
    Date date;
    try {
      final SimpleDateFormat sdf = new SimpleDateFormat(DateTime.DATE_FORMAT, Locale.ITALIAN);
      sdf.setLenient(
          false); // senza questo il 32/01 viene convertito in 01/02 senza dare eccezioni.
      date = sdf.parse(value);

    } catch (ParseException e) {
      date = null;
    }
    return date;
  }

  /**
   * Converte una stringa in un boolean.
   *
   * @param value stringa con il valore da convertire.
   * @return Ritorna true se la stringa è "S", false altrimenti.
   */
  public static boolean toBoolean(final String value) {
    return !value.isEmpty() && toBoolean(value.charAt(0));
  }

  /**
   * Converte un char in un boolean.
   *
   * @param value char con il valore da convertire.
   * @return Ritorna true se il carattere è "S", false altrimenti.
   */
  public static boolean toBoolean(Character value) {
    value = Character.toUpperCase(value);
    return (value.equals('S'));
  }

  /**
   * Converte un boolean in un char (S/N).
   *
   * @param value booleano da convertire.
   * @return Ritorna 'S' se value è true, 'N' altrimenti.
   */
  public static char toChar(final boolean value) {
    return (value) ? 'S' : 'N';
  }

  /**
   * Converte una stringa in un intero.
   *
   * @param value numero sotto forma di stringa da convertire.
   * @return ritorna un Integer rappresentate il numero passato come stringa. In caso di errore
   *     ritorna 0.
   */
  public static Integer toInteger(final String value) {
    try {
      return Integer.parseInt(value.trim());
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Converte un long in un intero.
   *
   * @param value numero sotto forma di long.
   * @return ritorna un Integer rappresentate il numero passato come long. In caso di errore ritorna
   *     0.
   */
  public static Integer toInteger(final long value) {
    try {
      return (int) value;
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Converte una stringa in un long.
   *
   * @param value numero sotto forma di stringa da convertire.
   * @return ritorna un Long rappresentante il numero passato come stringa. In caso di errore
   *     ritorna 0L.
   */
  public static Long toLong(final String value) {
    try {
      return Long.parseLong(value.trim());
    } catch (Exception e) {
      return 0L;
    }
  }

  /**
   * Converte una stringa in un numero decimale. Si occupa anche di convertire il carattere di
   * separazione da virgola a punto.
   *
   * @param value numero sotto forma di stringa da convertire.
   * @return ritorna un Float rappresentate il numero passato come stringa. In caso di errore
   *     ritorna 0.
   */
  public static Float toFloat(final String value) {
    try {
      return Float.parseFloat(value.replaceAll(",", "."));
    } catch (Exception e) {
      return 0f;
    }
  }

  /**
   * Converte un oggetto List<T> in un array T[].
   *
   * @param clazz oggetto Class per il tipo T.
   * @param list lista da convertire.
   * @return ritorna un array contenente gli oggetti di tipo T passati come paramentro nella lista.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(final Class<T> clazz, final List<T> list) {
    final T[] arr = (T[]) Array.newInstance(clazz, list.size());
    int i = 0;
    for (final T value : list) {
      arr[i++] = value;
    }
    return arr;
  }

  /**
   * Converte un array T[] in un oggetto List<T>.
   *
   * @param data array da convertire.
   * @return ritorna un oggetto List<T> con all'interno i dati letti dall'array di partenza.
   */
  public static <T> List<T> toList(final T[] data) {
    final List<T> list = new ArrayList<>();
    Collections.addAll(list, data);
    return list;
  }

  /**
   * Permette di ottenere l'indirizzo ip partendo da un host name. Se l'indirizzo è già un indirizzo
   * IP, non effettua nessuna conversione.
   *
   * @param hostname nome di rete per il quale ottenere l'indirizzo ip.
   * @return ritorna l'indirizzo ip richiesto.
   */
  public static String toIpAddress(final String hostname) {
    String ipAddress;

    try {
      // se hostname inizia con un numero do' per scontato che sia già un indirizzo IP
      if (Character.isDigit(hostname.charAt(0))) {
        ipAddress = hostname;

      } else {
        final InetAddress address = InetAddress.getByName(hostname);
        ipAddress = address.getHostAddress();
      }

    } catch (Exception e) {
      ipAddress = hostname;
    }

    return ipAddress;
  }

  /**
   * Converte un numero cardinale in una stringa con un numero ordinale. Per es: 1 -> "primo".
   *
   * @param value numero cardinale da convertire
   * @return ritorna una stringa con il numero passato in formato ordinale.
   */
  public static String toOrdinalNumber(final Integer value) {
    return switch (value) {
      case 1 -> "primo";
      case 2 -> "secondo";
      case 3 -> "terzo";
      case 4 -> "quarto";

      // da estendere se richiesto..

      default -> "";
    };
  }

  /**
   * Converte un numero cardinale in una stringa con un numero ordinale. Per es: 1 -> "primo".
   *
   * @param value numero cardinale da convertire.
   * @param capitalizeFirstLetter indica se la prima lettera del numero ordinale deve essere
   *     maiuscola o minuscola.
   * @return ritorna una stringa con il numero passato in formato ordinale.
   */
  public static String toOrdinalNumber(final Integer value, final boolean capitalizeFirstLetter) {
    String result = toOrdinalNumber(value);
    if (capitalizeFirstLetter && !Validator.isStringNullOrEmpty(result)) {
      result = result.substring(0, 1).toUpperCase() + result.substring(1);
    }
    return result;
  }

  /**
   * Converte un booleano in una stringa nel formato letto da Concilia (S = true; N = false).
   *
   * @param value valore booleano da convertire.
   * @return ritorna "S" se value è true, "N" altrimenti.
   */
  public static String toString(final boolean value) {
    return (value) ? "S" : "N";
  }

  /**
   * Converte un intero in una stringa.
   *
   * @param value numero intero da convertire.
   * @return ritorna una stringa che rappresenta il valore del numero intero passato come parametro.
   */
  public static String toString(final Integer value) {
    return value.toString();
  }

  /**
   * Converte un numero decimale (Float) in una stringa rispettando i separatori usati nella lingua
   * italiana.
   *
   * @param value numero decimale da convertire.
   * @return ritorna una stringa rappresentante il numero decimale.
   */
  public static String toItalianLocaleString(final float value) {
    final NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALY);
    numberFormat.setGroupingUsed(false);
    return numberFormat.format(value);
  }
}
