package me.timur.servicesearchtelegrambot.bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 15/08/22.
 */

public class KeyboardUtil {

    public static InlineKeyboardMarkup webAppKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        WebAppInfo info = new WebAppInfo("https://telegram.mihailgok.ru");
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setWebApp(info);
        button.setText("just test");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        List<List<InlineKeyboardButton>>  rows = new ArrayList<>();
        rows.add(row);
        keyboard.setKeyboard(rows);

        return keyboard;
    }

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
        ReplyKeyboardMarkup keyboard = replyKeyboardMarkup();
        keyboard.setKeyboard(rows);
        return keyboard;
    }


    public static ReplyKeyboardMarkup phoneRequest() {
        KeyboardButton button = new KeyboardButton("Поделится телефоном");
        button.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(button);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row);

        ReplyKeyboardMarkup keyboard = replyKeyboardMarkup();

        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    private static ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(false);
        return keyboard;
    }

    public static ReplyKeyboardRemove removeKeyBoard() {
        return new ReplyKeyboardRemove(true, false);
    }
}
