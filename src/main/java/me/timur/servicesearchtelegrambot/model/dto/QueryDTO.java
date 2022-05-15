package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDTO extends BaseDTO  {
    private Long id;
    @JsonProperty("date_created")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN) @JsonDeserialize(using = DateDeserializers.TimestampDeserializer.class)
    private Timestamp dateCreated;
    private UserDTO client;
    private ServiceProviderDTO provider;
    private ServiceDTO service;
    private String status;
    @JsonProperty("is_active")
    private Boolean isActive;

    public QueryDTO(Query query) {
        this.id = query.getId();
        this.dateCreated = query.getDateCreated();
        this.client = new UserDTO(query.getClient());
        this.provider = new ServiceProviderDTO(query.getProvider());
        this.service = new ServiceDTO(query.getService());
//        this.status = query.getStatus().getName();
        this.isActive = query.getIsActive();
    }
}
