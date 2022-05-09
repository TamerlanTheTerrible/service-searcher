package me.timur.servicesearchtelegrambot.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
public class ServiceProviderDto implements Serializable {
    private Long id;
    private String dateCreated;
    private ServiceDto service;
    private Boolean isActive;
}
