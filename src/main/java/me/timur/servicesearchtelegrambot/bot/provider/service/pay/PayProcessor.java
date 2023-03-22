package me.timur.servicesearchtelegrambot.bot.provider.service.pay;

import me.timur.servicesearchtelegrambot.bot.provider.dto.PayProcessorType;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayRequestDto;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayResponseDto;

/**
 * Created by Temurbek Ismoilov on 22/03/23.
 */

public interface PayProcessor {
    PayResponseDto pay(PayRequestDto requestDto);
    PayProcessorType getType();
}
