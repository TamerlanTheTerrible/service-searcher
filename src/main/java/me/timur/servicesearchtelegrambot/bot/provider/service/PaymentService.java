package me.timur.servicesearchtelegrambot.bot.provider.service;

import me.timur.servicesearchtelegrambot.bot.provider.dto.PayRequestDto;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayResponseDto;

/**
 * Created by Temurbek Ismoilov on 22/03/23.
 */

public interface PaymentService {

    PayResponseDto pay(PayRequestDto requestDto);
}
