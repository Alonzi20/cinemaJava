package it.unibo.samplejavafx.cinema.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javafx.util.Pair;

/**
 * Helper per la gestione di data e ora.
 */
public final class DateTime {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String COMPACT_DATE_FORMAT = "ddMMyyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String DATE_TIME_COMPLETE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String COMPACT_DATE_TIME_FORMAT = "ddMMyyyyHHmmss";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DAY_FORMAT = "dd";
    public static final String MONTH_FORMAT = "MM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String HOUR_FORMAT = "HH";
    public static final String MINUTE_FORMAT = "mm";
    private static final String UTC = "UTC";

    /**
     * Ritorna l'anno corrente.
     */
    public static int getCurrentYear() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Ritorna il numero del giorno corrente come stringa.
     */
    public static String getCurrentDayNumberString() {
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day < 10 ? "0" + day : "" + day;
    }

    /**
     * Ritorna il numero del mese corrente come stringa.
     */
    public static String getCurrentMonthNumberString() {
        final Calendar calendar = Calendar.getInstance();
        final int month = calendar.get(Calendar.MONTH) + 1;
        return month < 10 ? "0" + month : "" + month;
    }

    /**
     * Ritorna l'anno corrente come stringa.
     */
    public static String getCurrentYearString() {
        final Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * Ritorna l'ora corrente come stringa.
     */
    public static String getCurrentHourString() {
        final Calendar calendar = Calendar.getInstance();
        final int ora = calendar.get(Calendar.HOUR_OF_DAY);
        return ora < 10 ? "0" + ora : "" + ora;
    }

    /**
     * Ritorna il minuto corrente come stringa.
     */
    public static String getCurrentMinuteString() {
        final Calendar calendar = Calendar.getInstance();
        final int minute = calendar.get(Calendar.MINUTE);
        return minute < 10 ? "0" + minute : "" + minute;
    }

    /**
     * Ritorna la data corrente come stringa.
     */
    public static String getNowDateString() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
        return dateFormatter.format(calendar.getTime());
    }

    /**
     * Ritorna la data corrente come stringa nel formato COMPACT_DATE_FORMAT.
     */
    public static String getNowDateCompactString() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(COMPACT_DATE_FORMAT, Locale.ITALY);
        return dateFormatter.format(calendar.getTime());
    }

    /**
     * Ritorna l'ora corrente come stringa.
     */
    public static String getNowTimeString() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_FORMAT, Locale.ITALY);
        return dateFormatter.format(calendar.getTime());
    }

    /**
     * Ritorna la data e l'ora corrente come stringa nel formato COMPACT_DATE_FORMAT.
     */
    public static String getNowDateTimeCompactString() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(COMPACT_DATE_TIME_FORMAT, Locale.ITALY);
        return dateFormatter.format(calendar.getTime());
    }

    /**
     * Ritorna la data corrente come oggetto Date.
     */
    public static Date getNowDate() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * Ritorna la data corrente rappresentatata come totale dei millisecondi trascorsi dal 01/01/1970.
     */
    public static long getNowDateInMilliseconds() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * Permette di formattare una data a nel formato {@value #DATE_FORMAT}.
     *
     * @param date data da formattare.
     * @return ritorna una stringa con la data formattata.
     */
    public static String getFormattedDate(final Date date) {
        return getFormattedDate(date, DATE_FORMAT);
    }


    /**
     * Permette di formattare una data a seconda della stringa di formattazione passata come parametro.
     *
     * @param date   data da formattare.
     * @param format stringa di formato per la data.
     * @return ritorna una stringa con la data formattata.
     */
    public static String getFormattedDate(final Date date, final String format) {
        String formattedDate = "";

        try {
            if (date != null) {
                SimpleDateFormat outputFormatter = new SimpleDateFormat(format, Locale.ITALY);
                formattedDate = outputFormatter.format(date);
            }

        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }

    /**
     * Permette di formattare una data a seconda della stringa di formattazione passata come parametro.
     *
     * @param dateToFormat data da formattare come stringa.
     * @param outputFormat stringa di formato per la data.
     * @return ritorna una stringa con la data formattata.
     */
    public static String getFormattedDate(final String dateToFormat, final String outputFormat) {
        String formattedDate;

        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
            final Date date = inputFormatter.parse(dateToFormat);
            formattedDate = getFormattedDate(date, outputFormat);

        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }

    public static String getFormattedDateFromCompact(final String dateToFormat, final String outputFormat) {
        String formattedDate;

        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(COMPACT_DATE_FORMAT, Locale.ITALY);
            final Date date = inputFormatter.parse(dateToFormat);
            formattedDate = getFormattedDate(date, outputFormat);

        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }

    public static String getFormattedDateTime(final String dateTimeToFormat, final String outputFormat) {
        String formattedDate;

        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ITALY);
            final Date date = inputFormatter.parse(dateTimeToFormat);
            formattedDate = getFormattedDate(date, outputFormat);

        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }

    public static String getFormattedDateTimeFromFormat(final String dateTimeToFormat,
                                                        final String inputFormat,
                                                        final String outputFormat) {
        String formattedDate;

        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat, Locale.ITALY);
            final Date date = inputFormatter.parse(dateTimeToFormat);
            formattedDate = getFormattedDate(date, outputFormat);

        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }

    public static boolean isDateValid(final String dateString) {
        final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
        inputFormatter.setLenient(false);
        try {
            inputFormatter.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isTimeValid(final String timeString) {
        final SimpleDateFormat inputFormatter = new SimpleDateFormat(TIME_FORMAT, Locale.ITALY);
        inputFormatter.setLenient(false);
        try {
            inputFormatter.parse(timeString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static long getFormattedDate(final String dateToFormat) {
        Date date;

        final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
        inputFormatter.setTimeZone(TimeZone.getTimeZone(UTC));
        try {
            date = inputFormatter.parse(dateToFormat);
        } catch (ParseException e) {
            date = null;
        }

        return date != null ? date.getTime() : -1;
    }

    public static int getHour(final String timeString) {
        int endIndex = timeString.indexOf(":");

        if (endIndex != -1) {
            final String hourString = timeString.substring(0, endIndex);
            try {
                return Integer.parseInt(hourString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static int getMinutes(final String timeString) {
        int startIndex = timeString.indexOf(":");

        if (startIndex != -1) {
            final String minutesString = timeString.substring(++startIndex);
            try {
                return Integer.parseInt(minutesString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static Pair<Integer, Integer> getTime(final String timeString) {
        final String[] splittedTimeString = timeString.split(":");
        try {
            final int hours = Integer.parseInt(splittedTimeString[0]);
            final int minutes = Integer.parseInt(splittedTimeString[1]);
            return new Pair<>(hours, minutes);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTime(final int hour, final int minutes) {
        final String hh = getMinuteOrHour(hour);
        final String mm = getMinuteOrHour(minutes);
        return hh + ":" + mm;
    }

    public static String getMinuteOrHour(final int minuteOrHour) {
        return minuteOrHour < 10 ? "0" + minuteOrHour : "" + minuteOrHour;
    }

    public static String getFormattedDate(final long dateToFormat) {
        final Date date = new Date(dateToFormat);

        final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
        inputFormatter.setTimeZone(TimeZone.getTimeZone(UTC));
        return inputFormatter.format(date);
    }

    public static String getFormattedDate(final long dateToFormat, final String dateFormat) {
        final Date date = new Date(dateToFormat);

        final SimpleDateFormat inputFormatter = new SimpleDateFormat(dateFormat, Locale.ITALY);
        inputFormatter.setTimeZone(TimeZone.getTimeZone(UTC));
        return inputFormatter.format(date);
    }

    /**
     * Permette di convertire una stringa Data nel formato "{@value #DATE_FORMAT}" in un oggetto {@link Date}.
     *
     * @param dateToFormat data da convertire.
     * @return oggetto {@link Date} con la data formattata o {@code null} in caso di errore di parsing.
     */
    public static Date getDate(final String dateToFormat) {
        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
            return inputFormatter.parse(dateToFormat);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Permette di convertire una stringa Data nel formato specificato in un oggetto {@link Date}.
     *
     * @param dateToFormat Data da convertire.
     * @param format       Formato della data.
     * @return oggetto {@link Date} con la data formattata o {@code null} in caso di errore di parsing.
     */
    public static Date getDate(final String dateToFormat, final String format) {
        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(format, Locale.ITALY);
            return inputFormatter.parse(dateToFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromCompactDateTime(final String compactDateTime) {
        try {
            final String compactDate = compactDateTime.substring(0, compactDateTime.length() - 6);
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(COMPACT_DATE_FORMAT, Locale.ITALY);
            return inputFormatter.parse(compactDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormattedTime(final String timeToFormat) {
        return getFormattedTime(timeToFormat, TIME_FORMAT);
    }

    /**
     * Permette di formattare una data a nel formato {@value #TIME_FORMAT}.
     *
     * @param date data da formattare.
     * @return ritorna una stringa con la data formattata.
     */
    public static String getFormattedTime(final Date date) {
        return getFormattedDate(date, TIME_FORMAT);
    }

    /**
     * Permette di formattare un orario a seconda della stringa di formattazione passata come parametro.
     *
     * @param timeToFormat orario da formattare (HH:MM).
     * @param outputFormat stringa di formato per l'orario.
     * @return ritorna una stringa con l'orario formattato.
     */
    public static String getFormattedTime(final String timeToFormat, final String outputFormat) {
        String formattedTime;

        try {
            final SimpleDateFormat inputFormatter = new SimpleDateFormat(TIME_FORMAT, Locale.ITALY);
            final Date date = inputFormatter.parse(timeToFormat);
            formattedTime = getFormattedDate(date, outputFormat);

        } catch (Exception e) {
            formattedTime = "";
        }

        return formattedTime;
    }

    /**
     * Permette di formattare il between tra due date in formato yyyy-MM-DD
     *
     * @param from data inizio
     * @param to   data fine
     * @return ritorna una stringa contenente la query per il between tra due date
     */
    public static String getBetweenDatesAsString(final Date from, final Date to) {
        return String.format("substr(%1$s, 0, 11) between '%2$s' and '%3$s'",
                "DT_ACCERTAMENTO", getFormattedDate(from, SQL_DATE_FORMAT), getFormattedDate(to, SQL_DATE_FORMAT));
    }

    /**
     * Confronta due date tenendo conto del fatto che possono essere nulle.
     *
     * @param d1 Prima data da confrontare.
     * @param d2 Seconda data da confrontare.
     * @return Ritorna true se le due date sono uguali, false altrimenti.
     */
    public static boolean equals(final Date d1, final Date d2) {
        return d1 == null && d2 == null
                || d1 != null && d1.equals(d2);
    }

    /**
     * Converte il formato di rappresentazione di una data.
     *
     * @param date      Data da formattare
     * @param oldFormat formato originale della data.
     * @param newFormat formato convertito della data.
     * @return una stringa {@code vuota} in caso di errore di conversione.
     */
    public static String convertDate(final String date, final String oldFormat, final String newFormat) {
        final SimpleDateFormat oldDate = new SimpleDateFormat(oldFormat, Locale.ITALY);
        final SimpleDateFormat newDate = new SimpleDateFormat(newFormat, Locale.ITALY);
        try {
            return newDate.format(oldDate.parse(date.replace("T", " ")));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Controlla se il giorno è una domenica o se è un giorno Festivo secondo il calendario Italiano.
     *
     * @param giorno giorno da controllare.
     * @return {@code True} se festivo, {@code False} se feriale.
     */
    public static boolean isFestivo(final Date giorno) {
        // Festività Italiane a data fissa
        final String[] dateFestivita = new String[]{"01/01", "06/01", "25/04", "01/05", "02/06",
                "15/08", "01/11", "08/12", "25/12", "26/12"};

        // Nota: 'giorno' potrebbe avere l'orario, quindi confronto MONTH e DAY_OF_MONTH
        final Calendar calendarG = new GregorianCalendar(Locale.ITALIAN);
        calendarG.setTime(giorno);

        // Controllo se è una Domenica (quindi anche il giorno di Pasqua)
        if (calendarG.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            return true;

        // Controllo se è una Festività a data fissa
        int year = calendarG.get(Calendar.YEAR);
        for (final String dataFestivita : dateFestivita) {
            final String[] daymon = dataFestivita.split("/");
            // MONTH parte da 0
            if ((calendarG.get(Calendar.MONTH) + 1) == Integer.parseInt(daymon[1]) &&
                    calendarG.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(daymon[0]))
                return true;
        }

        // Se non è il Lunedì dell'Angelo allora è un giorno feriale.
        final Calendar calendarP = new GregorianCalendar(Locale.ITALIAN);
        calendarP.setTime(getPasqua(year));
        calendarP.add(Calendar.DAY_OF_MONTH, 1);
        // Non importa che MONTH parte da 0
        return calendarG.get(Calendar.MONTH) == calendarP.get(Calendar.MONTH)
                && calendarG.get(Calendar.DAY_OF_MONTH) == calendarP.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Preso da <a href="https://it.wikibooks.org/wiki/Implementazioni_di_algoritmi/Calcolo_della_Pasqua#Java">...</a>
     *
     * @param year anno di cui calcolare il giorno di Pasqua.
     * @return giorno di Pasqua calcolata secondo l'algoritmo di Gauss.
     */
    public static Date getPasqua(final int year) {

        final int a = year % 19;
        final int b = year % 4;
        final int c = year % 7;

        int m = 0;
        int n = 0;

        // Giusto per stare larghi...
        if ((year >= 1583) && (year <= 1699)) {
            m = 22;
            n = 2;
        }
        if ((year >= 1700) && (year <= 1799)) {
            m = 23;
            n = 3;
        }
        if ((year >= 1800) && (year <= 1899)) {
            m = 23;
            n = 4;
        }
        if ((year >= 1900) && (year <= 2099)) {
            m = 24;
            n = 5;
        }
        if ((year >= 2100) && (year <= 2199)) {
            m = 24;
            n = 6;
        }
        if ((year >= 2200) && (year <= 2299)) {
            m = 25;
        }
        if ((year >= 2300) && (year <= 2399)) {
            m = 26;
            n = 1;
        }
        if ((year >= 2400) && (year <= 2499)) {
            m = 25;
            n = 1;
        }

        int d = (19 * a + m) % 30;
        int e = (2 * b + 4 * c + 6 * d + n) % 7;

        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);

        if (d + e < 10) {
            calendar.set(Calendar.MONTH, Calendar.MARCH);
            calendar.set(Calendar.DAY_OF_MONTH, d + e + 22);
        } else {
            calendar.set(Calendar.MONTH, Calendar.APRIL);
            int day = d + e - 9;

            if (26 == day)
                day = 19;
            if ((25 == day) && (28 == d) && (e == 6) && (a > 10))
                day = 18;

            calendar.set(Calendar.DAY_OF_MONTH, day);
        }

        return calendar.getTime();
    }

    /**
     * Ritorna il primo giorno feriale a partire dalla data passate con un eventuale spostamento di
     * giorni (in avanti o in dietro).
     *
     * @param dataBase    data da cui partire a cercare il prossimo giorno feriale.
     * @param spostamento numero di giorni in avanti o indietro rispetto alla data base da cui
     *                    iniziare a cercare il feriale. Si possono passare valori <, >, = a 0.
     * @return prossimo giorno feriale
     */
    public static Date getProssimoFeriale(final Date dataBase, final int spostamento) {
        final Calendar scadenza = new GregorianCalendar();
        scadenza.setTime(dataBase);
        scadenza.add(Calendar.DAY_OF_MONTH, spostamento);

        while (DateTime.isFestivo(scadenza.getTime()))
            scadenza.add(Calendar.DAY_OF_YEAR, 1);

        return scadenza.getTime();
    }

    public static long getDaysBetween(final Date d1, final Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date parse(final String dateString, final String outputFormat) {
        final DateFormat formatter = new SimpleDateFormat(outputFormat, Locale.ITALIAN);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
