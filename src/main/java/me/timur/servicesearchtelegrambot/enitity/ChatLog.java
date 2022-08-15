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
@Table(name = "chat_log")
public class ChatLog extends BaseEntity{
    @Column(name = "id")
    private Long id;

    @Column(name = "tg_user_id")
    private Long tgUserId;

    @Column(name = "tg_chat_id")
    private String tgChatId;

    @Column(name = "message")
    private String message;

    public ChatLog(Long tgUserId, String tgChatId, String message) {
        this.tgUserId = tgUserId;
        this.tgChatId = tgChatId;
        this.message = message;
    }
}
