package me.timur.servicesearchtelegrambot.bot.util;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhoneUtil {
    private final static String PHONE_PATTERN = "^(?=998)(?=[0-9]{9}).*";

    public static boolean isValid(String phone) {
        if (Objects.isNull(phone)) {
            return false;
        }

        return phone.replace(" ", "").matches(PHONE_PATTERN);
    }
}
