package it.unibo.samplejavafx.cinema.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

  /**
   * @param s stringa su cui eseguire il padding
   * @param length lunghezza massima della stringa di ritorno
   * @return ritorna la stringa in input con eventuali caratteri blank alla sua destra
   */
  public static String padRight(final String s, final int length) {
    return String.format("%1$-" + length + "s", (s != null) ? s : "");
  }

  /**
   * @param s stringa su cui eseguire il padding
   * @param length lunghezza massima della stringa di ritorno
   * @return ritorna la stringa in input con eventuali caratteri blank alla sua sinistra
   */
  public static String padLeft(final String s, final int length) {
    return String.format("%1$#" + length + "s", (s != null) ? s : "");
  }

  /**
   * Riempie una stringa con degli zeri
   *
   * @param n numero in input
   * @param length lunghezza massima della stringa di ritorno
   * @return ritorna una stringa contente il numero in input ed eventuali zeri davanti
   */
  public static String fillWithZeros(final int n, final int length) {
    return String.format("%0" + length + "d", n);
  }

  /**
   * Formatta un numero decimale come una valuta.
   *
   * @param currency Valore da formattare.
   * @return Ritorna una stringa formattata come valuta.
   */
  public static String formatCurrency(final Float currency) {
    try {
      final DecimalFormat decimalFormatter = new DecimalFormat("#0.00");
      return decimalFormatter.format(currency);

    } catch (Exception e) {
      return (currency == null) ? "0" : currency.toString();
    }
  }

  /**
   * Formatta un numero decimale secondo una stringa di formato.
   *
   * @param f Numero da formattare.
   * @param format Stringa di formato.
   * @return Ritorna una stringa formattata secondo i parametri di input.
   */
  public static String formatFloat(final Float f, final String format) {
    try {
      final DecimalFormat decimalFormatter = new DecimalFormat(format);
      return decimalFormatter.format(f);

    } catch (Exception e) {
      return f.toString();
    }
  }

  /**
   * Imposta maiuscola la prima lettera di una stringa.
   *
   * @param string Testo di riferimento.
   * @return Ritorna una nuova stringa con la prima lettera maiuscola.
   */
  public static String capitalizeFirstLetter(final String string) {
    if (string != null) {
      if (string.length() > 1) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
      } else {
        return string.toUpperCase();
      }
    } else {
      return null;
    }
  }

  /**
   * Rimuove l'ultimo carattere dalla stringa se quest'ultimo è corrispondente a un certo valore
   *
   * @param string Testo di riferimento.
   * @param character Carattere da eliminare.
   * @return Ritorna una nuova stringa con l'ultimo carattere eliminato se quest'ultimo è
   *     corrispondente a un certo valore.
   */
  public static String removeLastChar(final String string, final char character) {
    if (string == null || string.length() == 0) {
      return string;
    }
    if (string.charAt(string.length() - 1) == character)
      return string.substring(0, string.length() - 1);

    return string;
  }

  /**
   * Rimuove gli ultimi caratteri alla fine della stringa
   *
   * @param string Testo di riferimento.
   * @param character Carattere da eliminare.
   * @return Ritorna una nuova stringa senza il carattere selezionato in fondo alla stessa (ad
   *     esempio senza spazi)
   */
  public static String trimEnd(String string, final char character) {
    for (int i = string.length() - 1; i >= 0; --i) {
      if (string.charAt(i) != character) {
        string = string.substring(0, (i + 1));
        break;
      }
    }
    return string;
  }

  /**
   * Controlla il valore del booleano e lo tramuta in stringa
   *
   * @param input booleano da convertire
   * @return Ritorna il valore in stringa a partire da un flag
   */
  public static String toNumeralString(final Boolean input) {
    if (input == null) {
      return "null";
    } else {
      return input ? "S" : "N";
    }
  }

  /**
   * Confronta due stringhe tenendo presente che null corrisponde a stringa vuota!
   *
   * @param a Prima stringa
   * @param b Seconda stringa
   * @return Ritorna true se le due stringhe sono uguali
   */
  public static boolean equals(String a, String b) {
    if (a == null) a = "";
    if (b == null) b = "";
    return a.equals(b);
  }

  /**
   * Controlla se la stringa inserita è un intero
   *
   * @param str Stringa da verificare
   * @return Ritorna true se il numero è intero
   */
  public static boolean isInteger(final String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }

  /**
   * Inserisce uno spazio ogni {@code chars} caratteri della stringa in input.
   *
   * @param str Stringa da formattare.
   * @param chars Ogni quanti caratteri inserire uno spazio di separazione.
   * @return Stringa con caratteri raggruppati o la stringa originaria se la lunghezza è minore del
   *     numero di raggruppamento.
   */
  public static String splitEvery(final String str, final int chars) {
    if (str.length() <= chars) return str;

    final StringBuilder res = new StringBuilder();
    int pos = 0;
    final int bits = (int) Math.floor(str.length() / chars);

    for (int i = 0; i < bits; i++) {
      res.append(str, pos, pos + chars).append(" ");
      pos += chars;
    }
    res.append(" ").append(str.substring(pos));
    return res.toString().trim();
  }

  public static String toLetters(final float number) {
    if (number == 0) return "zero";

    final int n = Math.round(number * 100);
    final int parteIntera = n / 100;
    final String parteDecimale = String.format("%02d", n % 100);

    if (parteIntera == 0) {
      return "zero/" + parteDecimale;
    } else {
      return NumberToTextRicorsiva(parteIntera) + "/" + parteDecimale;
    }
  }

  private static String NumberToTextRicorsiva(final int n) {
    if (n < 0) {
      return "meno " + NumberToTextRicorsiva(-n);
    } else if (n == 0) {
      return "";
    } else if (n <= 19) {
      return new String[] {
            "uno",
            "due",
            "tre",
            "quattro",
            "cinque",
            "sei",
            "sette",
            "otto",
            "nove",
            "dieci",
            "undici",
            "dodici",
            "tredici",
            "quattordici",
            "quindici",
            "sedici",
            "diciassette",
            "diciotto",
            "diciannove"
          }
          [n - 1];
    } else if (n <= 99) {
      final String[] vettore = {
        "venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta", "ottanta", "novanta"
      };
      String letter = vettore[n / 10 - 2];
      int t = n % 10; // t è la prima cifra di n
      // se è 1 o 8 va tolta la vocale finale di letter
      if (t == 1 || t == 8) {
        letter = letter.substring(0, letter.length() - 1);
      }
      return letter + NumberToTextRicorsiva(n % 10);
    } else if (n <= 199) {
      return "cento" + NumberToTextRicorsiva(n % 100);
    } else if (n <= 999) {
      int m = n % 100;
      m /= 10; // divisione intera per 10 della variabile
      String letter = "cent";
      if (m != 8) {
        letter = letter + "o";
      }
      return NumberToTextRicorsiva(n / 100) + letter + NumberToTextRicorsiva(n % 100);
    } else if (n <= 1999) {
      return "mille" + NumberToTextRicorsiva(n % 1000);
    } else if (n <= 999999) {
      return NumberToTextRicorsiva(n / 1000) + "mila" + NumberToTextRicorsiva(n % 1000);
    } else if (n <= 1999999) {
      return "unmilione" + NumberToTextRicorsiva(n % 1000000);
    } else if (n <= 999999999) {
      return NumberToTextRicorsiva(n / 1000000) + "milioni" + NumberToTextRicorsiva(n % 1000000);
    } else if (n <= 1999999999) {
      return "unmiliardo" + NumberToTextRicorsiva(n % 1000000000);
    } else {
      return NumberToTextRicorsiva(n / 1000000000)
          + "miliardi"
          + NumberToTextRicorsiva(n % 1000000000);
    }
  }

  /**
   * Calcola il numero di occorrenze del carattere nella stringa.
   *
   * @param text Stringa da controllare.
   * @param c Carattere di cui contare le occorrenze.
   * @return 0 se stringa è nulla o di lunghezza 0, altrimenti il numero di occorrenze (>= 0)
   */
  public static int countOccur(final String text, final char c) {
    if (text == null || text.equals("")) return 0;
    // Lunghezza totale - lunghezza della stessa stringa meno il carattere da contare
    return text.length() - text.replace("" + c, "").length();
  }

  /**
   * Restituisce la sottostringa che si trova tra due sottostringhe della stringa data, se presente.
   *
   * @param string Stringa da controllare.
   * @param before Stringa precedente alla sottostringa da cercare.
   * @param after Stringa successiva alla sottostringa da cercare.
   * @return la sottostringa cercata se presente, altrimenti null.
   */
  public static String substringBetween(
      final String string, final String before, final String after) {
    String substring = null;
    final Pattern pattern = Pattern.compile(before + "(.*?)" + after);
    final Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      substring = matcher.group(1);
    }
    return substring;
  }

  /**
   * Restituisce la parola (sottostringa fino allo spazio) che si trova dopo la sottostringa
   * indicata all'interno della stringa data, se presente.
   *
   * @param string Stringa da controllare.
   * @param before Stringa precedente alla parola da cercare.
   * @return la parola cercata se presente, altrimenti null.
   */
  public static String wordAfter(final String string, final String before) {
    String word = null;
    final Pattern pattern = Pattern.compile(before + "(.*?)" + " ");
    final Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      word = matcher.group(1);
    }
    return word;
  }

  /**
   * Restituisce la parola (sottostringa fino allo spazio) che si trova prima della sottostringa
   * indicata all'interno della stringa data, se presente.
   *
   * @param string Stringa da controllare.
   * @param after Stringa successiva alla parola da cercare.
   * @return la parola cercata se presente, altrimenti null.
   */
  public static String wordBefore(final String string, final String after) {
    String word = null;
    final Pattern pattern = Pattern.compile(" " + "(.*?)" + after);
    final Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      word = matcher.group(1);
    }
    return word;
  }

  public static String removeAccents(final String value) {
    final Map<Character, Character> mapNorm = new HashMap<>();
    mapNorm.put('À', 'A');
    mapNorm.put('Á', 'A');
    mapNorm.put('Â', 'A');
    mapNorm.put('Ã', 'A');
    mapNorm.put('Ä', 'A');
    mapNorm.put('È', 'E');
    mapNorm.put('É', 'E');
    mapNorm.put('Ê', 'E');
    mapNorm.put('Ë', 'E');
    mapNorm.put('Í', 'I');
    mapNorm.put('Ì', 'I');
    mapNorm.put('Î', 'I');
    mapNorm.put('Ï', 'I');
    mapNorm.put('Ù', 'U');
    mapNorm.put('Ú', 'U');
    mapNorm.put('Û', 'U');
    mapNorm.put('Ü', 'U');
    mapNorm.put('Ò', 'O');
    mapNorm.put('Ó', 'O');
    mapNorm.put('Ô', 'O');
    mapNorm.put('Õ', 'O');
    mapNorm.put('Ö', 'O');
    mapNorm.put('Ñ', 'N');
    mapNorm.put('Ç', 'C');
    mapNorm.put('ª', 'A');
    mapNorm.put('º', 'O');
    mapNorm.put('§', 'S');
    mapNorm.put('³', '3');
    mapNorm.put('²', '2');
    mapNorm.put('¹', '1');
    mapNorm.put('à', 'a');
    mapNorm.put('á', 'a');
    mapNorm.put('â', 'a');
    mapNorm.put('ã', 'a');
    mapNorm.put('ä', 'a');
    mapNorm.put('è', 'e');
    mapNorm.put('é', 'e');
    mapNorm.put('ê', 'e');
    mapNorm.put('ë', 'e');
    mapNorm.put('í', 'i');
    mapNorm.put('ì', 'i');
    mapNorm.put('î', 'i');
    mapNorm.put('ï', 'i');
    mapNorm.put('ù', 'u');
    mapNorm.put('ú', 'u');
    mapNorm.put('û', 'u');
    mapNorm.put('ü', 'u');
    mapNorm.put('ò', 'o');
    mapNorm.put('ó', 'o');
    mapNorm.put('ô', 'o');
    mapNorm.put('õ', 'o');
    mapNorm.put('ö', 'o');
    mapNorm.put('ñ', 'n');
    mapNorm.put('ç', 'c');

    if (value == null) {
      return "";
    }

    final StringBuilder sb = new StringBuilder(value);
    for (int i = 0; i < value.length(); i++) {
      final Character c = mapNorm.get(sb.charAt(i));
      if (c != null) {
        sb.setCharAt(i, c);
      }
    }
    return sb.toString();
  }
}
