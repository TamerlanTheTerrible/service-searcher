package me.timur.servicesearchtelegrambot.controller;

import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;
import me.timur.servicesearchtelegrambot.service.QueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

@RestController
@RequestMapping("/query")
public record QueryController(
        QueryService queryService
) {

    @GetMapping("/{id}")
    public BaseResponse<QueryDTO> getById(@PathVariable Long id) {
        Query query = queryService.getById(id);
        return BaseResponse.payload(new QueryDTO(query));
    }

    @GetMapping("")
    public BaseResponse<List<QueryDTO>> getAll() {
        List<Query> queries = queryService.getAll();
        List<QueryDTO> queryDTOs = queries.stream().map(QueryDTO::new).toList();
        return BaseResponse.payload(queryDTOs);
    }

    @PutMapping("/{id}")
    public BaseResponse<NoopDTO> update(@PathVariable Long id, @RequestBody QueryDTO dto) {
        queryService.update(id, dto);
        return BaseResponse.payload();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<NoopDTO> delete(@PathVariable Long id) {
        queryService.delete(id);
        return BaseResponse.payload();
    }

    @PostMapping("")
    public BaseResponse<NoopDTO> saveQuery(@RequestBody QueryDTO dto){
        queryService.save(dto);
        return BaseResponse.payload();
    }
}
