package me.timur.servicesearchtelegrambot.bot.util;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class StringUtil {
    private final static String PHONE_PATTERN = "^(?=998)(?=[0-9]{9}).*";

    public static boolean isValidPhone(String phone) {
        if (Objects.isNull(phone)) {
            return false;
        }
        return phone.replace(" ", "").matches(PHONE_PATTERN);
    }

    public static boolean isLatin(String str) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(str);
    }
}
