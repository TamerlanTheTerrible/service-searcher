package me.timur.servicesearchtelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private ErrorPayload error = null;

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

    public static BaseResponse<NoopDTO> error(Exception e) {
        return BaseResponse.<NoopDTO>builder()
                .error(new ErrorPayload(e))
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ErrorPayload {
    private String name;
    private String type;
    private String message;

    public ErrorPayload(Exception e) {
        this.name = e.getClass().getSimpleName();
        this.type = e.getClass().getTypeName();
        this.message = e.getMessage();
    }
}
