package me.timur.servicesearchtelegrambot.bot.provider.service.pay.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayProcessorType;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayRequestDto;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayResponseDto;
import me.timur.servicesearchtelegrambot.bot.provider.service.pay.PayProcessor;
import org.springframework.stereotype.Service;

/**
 * Created by Temurbek Ismoilov on 22/03/23.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ClickProcessor implements PayProcessor {
    @Override
    public PayResponseDto pay(PayRequestDto requestDto) {
        return null;
    }

    @Override
    public PayProcessorType getType() {
        return PayProcessorType.CLICK;
    }
}
