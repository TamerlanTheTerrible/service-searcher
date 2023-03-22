package me.timur.servicesearchtelegrambot.bot.provider.service.pay;

import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayProcessorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 22/03/23.
 */

@Slf4j
@Component
public class PaymentProcessorFactory {

    private final EnumMap<PayProcessorType, PayProcessor> map;

    @Autowired
    public PaymentProcessorFactory(List<PayProcessor> payProcessors) {
        this.map = new EnumMap<>(PayProcessorType.class);
        payProcessors.forEach(
                p -> this.map.put(p.getType(), p)
        );
    }

    public PayProcessor getProcessor(PayProcessorType type) {
        return map.get(type);
    }
}
