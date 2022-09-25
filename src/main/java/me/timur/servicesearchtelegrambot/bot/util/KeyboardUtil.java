package me.timur.servicesearchtelegrambot.bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 15/08/22.
 */

public class KeyboardUtil {

    public static ReplyKeyboardMarkup keyboard(List<String> stringValues, Integer rowSize) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow(rowSize);
        Integer valuesCount = stringValues.size();
        Integer rowElementsLeft = rowSize;

        for (int i = 0; i < stringValues.size();) {
            //while row is not empty add new keyboardValue
            while (rowElementsLeft > 0 && valuesCount > 0) {
                row.add(stringValues.get(i));
                valuesCount--;
                rowElementsLeft--;
                i++;
            }
            //when row is full, add it to the row list and create a new row
            rowElementsLeft = rowSize;
            rows.add(row);
            row = new KeyboardRow(rowSize);
        }
        //create keyboard from keyboard row list
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(false);
        return keyboard;
    }

    public static ReplyKeyboardRemove removeKeyBoard() {
        return new ReplyKeyboardRemove(true, false);
    }
}
