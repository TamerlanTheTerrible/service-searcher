package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Temurbek Ismoilov on 15/05/22.
 */


@Data
@Builder
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@EqualsAndHashCode(callSuper = false)
public
class ErrorDTO extends BaseDTO {
    private String name;
    private String type;
    private String message;

    public ErrorDTO(Exception e) {
        this.name = e.getClass().getSimpleName();
        this.type = e.getClass().getTypeName();
        this.message = e.getMessage();
    }
}