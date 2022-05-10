package me.timur.servicesearchtelegrambot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.timur.servicesearchtelegrambot.model.Lang;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Temurbek Ismoilov on 08/02/22.
 */

@Converter
public class LangToJsonConverter implements AttributeConverter<Lang, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Lang lang) {
        lang.setRu(lang.getRu().trim().toLowerCase());
        lang.setUz(lang.getUz().trim().toLowerCase());
        return mapper.writeValueAsString(lang);
    }

    @SneakyThrows
    @Override
    public Lang convertToEntityAttribute(String s) {
        return mapper.readValue(s, Lang.class);
    }
}
