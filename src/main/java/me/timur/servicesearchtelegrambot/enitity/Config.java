package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.bot.ConfigName;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 30/01/23.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "config")
public class Config extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private ConfigName name;

    @Column(name = "value", nullable = false)
    private String value;

}
