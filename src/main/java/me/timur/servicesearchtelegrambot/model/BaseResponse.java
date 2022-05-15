package me.timur.servicesearchtelegrambot.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import me.timur.servicesearchtelegrambot.model.dto.BaseDTO;
import me.timur.servicesearchtelegrambot.model.dto.ErrorDTO;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;

/**
 * Created by Temurbek Ismoilov on 07/02/22.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private T payload = null;
    private ErrorDTO error = null;

    public static <T> BaseResponse<T> payload(T payload){
        return BaseResponse.<T>builder()
                .payload(payload)
                .build();
    }

    public static BaseResponse<NoopDTO> payload(){
        return BaseResponse.<NoopDTO>builder()
                .payload(NoopDTO.INSTANCE)
                .build();
    }

    public static BaseResponse<ErrorDTO> error(Exception e) {
        return BaseResponse.<ErrorDTO>builder()
                .error(new ErrorDTO(e))
                .build();
    }
}

