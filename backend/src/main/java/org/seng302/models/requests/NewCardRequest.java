package org.seng302.models.requests;

import java.util.Date;
import org.seng302.models.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class NewCardRequest {
    private String title;
    private String description;
    private String keywords;
    private User user;
    private MarketplaceSection section;

    public NewCardRequest(User user, String title, String description, String keywords, MarketplaceSection section) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.user = user;
        this.section = section;
    }
}
