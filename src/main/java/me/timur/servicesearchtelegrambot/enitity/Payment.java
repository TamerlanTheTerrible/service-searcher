package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.bot.provider.dto.PayProcessorType;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Temurbek Ismoilov on 22/03/23.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Column(name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayStatus status;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ProviderService service;

    @Column(name = "external_id")
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "processor")
    private PayProcessorType processor;

    @Column(name = "processing_time")
    private LocalDateTime processingTime;

}

enum PayStatus {
    NEW,
    SUCCESS,
    FAILED,
    RETURNED
}




