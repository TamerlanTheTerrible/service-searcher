package me.timur.servicesearchtelegrambot.service;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;
import me.timur.servicesearchtelegrambot.repository.QueryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Service @Primary
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private final QueryRepository queryRepository;
    private final UserService userService;
    private final ServiceProviderService providerService;
    private final ServiceManager serviceManager;

    @Override
    public Long save(QueryDTO dto) {
        User client = userService.getActiveUserById(dto.getClient().getId());
        ServiceProvider provider = providerService.getActiveById(dto.getProvider().getId());
        me.timur.servicesearchtelegrambot.enitity.Service service = serviceManager.getActiveServiceById(dto.getService().getId());

        Query query = new Query(client, provider, service);
        return queryRepository.save(query).getId();
    }

    @Override
    public Query getById(Long id) {
        return queryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find query with id %s", id)));
    }

    @Override
    public List<Query> getAll() {
        return queryRepository.findAll();
    }

    @Override
    public void update(Long id, QueryDTO dto) {
        Query query = getById(id);
        ServiceProvider provider = new ServiceProvider();
        provider.setId(requireNonNullElse(dto.getProvider().getId(), query.getProvider().getId()));
        query.setProvider(provider);
//        query.getStatus().setName(requireNonNullElse(dto.getStatus(), query.getStatus().getName()));
        queryRepository.save(query);
    }

    @Override
    public void delete(Long id) {
        Query query = getById(id);
        query.setIsActive(false);
        queryRepository.save(query);
    }
}
