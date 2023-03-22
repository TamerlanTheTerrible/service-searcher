package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.bot.Region;
import me.timur.servicesearchtelegrambot.enitity.ProviderServiceSubscription;
import me.timur.servicesearchtelegrambot.enitity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProviderServiceSubscriptionRepository extends JpaRepository<ProviderServiceSubscription, Long> {
    List<ProviderServiceSubscription> findAllByProviderServiceServiceAndProviderServiceProviderRegionAndEndDateAfterOrderByStartDateDesc(Service service, Region region, LocalDate now);
    List<ProviderServiceSubscription> findAllByEndDateEquals(LocalDate endDate);

}