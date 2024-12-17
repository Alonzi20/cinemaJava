package it.unibo.samplejavafx.cinema.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Validator {
  public static boolean isStringNullOrEmpty(final String s) {
    try {
      return (s == null) || ((s.isEmpty()));
    } catch (Exception e) {
      return true;
    }
  }

  public static boolean isCollectionNullOrEmpty(final Collection<?> collection) {
    return (collection == null) || (collection.isEmpty());
  }

  public static boolean isSetNullOrEmpty(final Set<?> set) {
    return (set == null) || (set.isEmpty());
  }

  public static boolean isListNullOrEmpty(final List<?> list) {
    return (list == null) || (list.isEmpty());
  }

  public static boolean isMapNullOrEmpty(final Map<?, ?> map) {
    return (map == null) || (map.isEmpty());
  }

  public static boolean isValidDate(final String date) {
    return (Convert.toDate(date) != null);
  }
}
