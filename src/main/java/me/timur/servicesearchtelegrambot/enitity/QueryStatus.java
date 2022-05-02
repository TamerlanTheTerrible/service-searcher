package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */
@Data
@Entity
@Table(name = "query_status")
public class QueryStatus {

    @Id
    @Column(name = "name", nullable = false)
    private String name;
}
