package org.seng302.models.requests;

import java.util.Date;
import org.seng302.models.*;
import org.seng302.models.Address;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class NewCardRequest {
    private String userName;
    private Address userHomeAddress;

    private String title;
    private String description;
    private Date created;
    private String keywords;
    private MarketplaceSection cardType;

    public NewCardRequest(String userName, Address userHomeAddress, String title, String description, Date created, String keywords, MarketplaceSection cardType) {
        this.userName = userName;
        this.userHomeAddress = userHomeAddress;
        this.title = title;
        this.description = description;
        this.created = created;
        this.keywords = keywords;
        this.cardType = cardType;
    }
}
