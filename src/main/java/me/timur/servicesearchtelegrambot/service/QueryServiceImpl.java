package me.timur.servicesearchtelegrambot.service;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.model.dto.QueryDto;
import me.timur.servicesearchtelegrambot.repository.QueryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Service @Primary
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private QueryRepository queryRepository;

    @Override
    public void save(QueryDto dto) {
        queryRepository.save(new Query(dto));
    }
}
