package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

@Transactional
public interface QueryService {

    Long save(QueryDTO dto);

    Query getById(Long id);

    List<Query> getAll();

    void update(Long id, QueryDTO dto);

    void delete(Long id);

}
