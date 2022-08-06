package me.timur.servicesearchtelegrambot.enitity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Temurbek Ismoilov on 03/08/22.
 */

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class ChatLog extends BaseEntity{
    @Column(name = "id")
    private Long id;

    @Column(name = "tg_chat_id")
    private Long tgChatId;

    @Column(name = "message")
    private String message;

    public ChatLog(Long tgChatId, String message) {
        this.tgChatId = tgChatId;
        this.message = message;
    }
}
