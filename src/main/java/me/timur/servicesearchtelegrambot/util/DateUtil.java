package me.timur.servicesearchtelegrambot.util;

import me.timur.servicesearchtelegrambot.exception.InvalidInputException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by Temurbek Ismoilov on 06/02/22.
 */

public class DateUtil {
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static LocalDateTime stringToDateTimeOrNull(String dateInString) {
        if (dateInString != null){
            try {
                return LocalDateTime.parse(addTimeIfAbsent(dateInString), DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e){
                throw new InvalidInputException(String.format("Required pattern %s: ", DATE_TIME_PATTERN), e);
            }
        }
        else
            return null;
    }

    public static String dateTimeToString(LocalDateTime date) {
        if (date != null){
            try {
                return date.format(DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e){
                throw new InvalidInputException("Error while formatting " + date + "to " + DATE_TIME_FORMATTER, e);
            }
        }
        else
            return null;
    }

    public static String addTimeIfAbsent(String rawDateTime) {
        if (rawDateTime.length() == DATE_PATTERN.length())
            return rawDateTime + " 00:00";
        else if (rawDateTime.length() == DATE_TIME_PATTERN.length())
            return rawDateTime;
        else
            throw new InvalidInputException(String.format("Required patterns: %s, %s", DATE_TIME_PATTERN, DATE_PATTERN));
    }

    public static String formatDateInString(String dateInString) {
        final LocalDateTime date = stringToDateTimeOrNull(dateInString);
        return dateTimeToString(date);
    }

}
