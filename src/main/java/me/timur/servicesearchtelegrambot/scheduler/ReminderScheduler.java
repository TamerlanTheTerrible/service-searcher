package me.timur.servicesearchtelegrambot.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.enitity.ProviderServiceSubscription;
import me.timur.servicesearchtelegrambot.repository.ProviderServiceSubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 13/03/23.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ProviderServiceSubscriptionRepository subscriptionRepository;

    private final static int DAYS_LEFT = 1;
    private final static String REMINDER_TEXT = "Напоминаем, у Вас осталось 1 день по подписке на \"%s\". " +
            "Для продления подписке, просим осуществить оплату";

    @Scheduled(cron="0 0 10 * * *")
    void paymentReminder() {
        // fetch all subscriptions with 1 day left and group by provider
        final List<ProviderServiceSubscription> subscriptions = subscriptionRepository
                .findAllByEndDateEquals(LocalDate.now().plusDays(DAYS_LEFT))
                .stream().toList();

        for (ProviderServiceSubscription subscription: subscriptions) {
            Long chatId = subscription.getProviderService().getProvider().getUser().getTelegramId();
            String text = String.format(REMINDER_TEXT, subscription.getProviderService().getService().getName());
            sendReminder(chatId, text);
        }
    }

    private void sendReminder(Long chatId, String text) {
        //TODO
    }
}
