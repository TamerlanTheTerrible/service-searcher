package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.bot.Region;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

@Transactional
public interface QueryService {

    Query save(QueryDTO dto);

    Query save(Query query);

    Query getById(Long id);

    List<Query> getAll();

    void update(Long id, QueryDTO dto);

    void deactivate(Long id);

    void delete(Query query);

    List<Query> getAllActiveByClientTgId(Long tgId);

    Optional<Query> getLastActiveByClientTgId(Long tgId);

    List<Query> getAllByServicesAndRegion(List<Service> services, Region region);

    void closeAll(String chatId);
}
