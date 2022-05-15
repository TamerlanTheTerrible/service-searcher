package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

@Transactional
public interface QueryService {
    void save(QueryDTO dto);
}
