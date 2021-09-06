package org.seng302.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;


    private long cardId;
    private String title;
    private Date displayPeriodEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;

    private Boolean read;

    /**
     * Empty constructor for JPA use.
     */
    protected Notification() {
    }

    public Notification(long userId, long cardId, String title, Date displayPeriodEnd) {
        this.userId = userId;
        this.cardId = cardId;
        this.title = title;
        this.displayPeriodEnd = displayPeriodEnd;
        this.status = NotificationStatus.EXPIRED;
        this.created = new Date();
        this.read = false;
    }

    /***
     * Sets notification status to deleted
     */
    public void setDeleted() {
        status = NotificationStatus.DELETED;
    }
}