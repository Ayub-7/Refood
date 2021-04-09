package org.seng302.models;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.seng302.utilities.serializers.PrimaryAdministratorSerializer;

import javax.persistence.*;

import java.util.*;


@Getter @Setter // generate setters and getters for all fields (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Forces any nested business objects to only use name to prevent recursion.
@JsonPropertyOrder({"id", "administrators", "name", "primaryAdministratorId"}) // force json property order to match api.
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinTable(name = "BUSINESS_ADMINS",
                    joinColumns = @JoinColumn(name="BUSINESS_ID"),
                    inverseJoinColumns = @JoinColumn(name="USER_ID"))
    private List<User> administrators = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonSerialize(using = PrimaryAdministratorSerializer.class)
    @JsonProperty("primaryAdministratorId") // while the entity itself stores the user object, when we output to a JSON,
    private User primaryAdministrator; // it will only use the id as the value, and key as primaryAdministratorId.

    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date created;

    protected Business() {}

    /**
     * Constructor for the Business object
     * @param name  Name of the business
     * @param description   Brief description of the business
     * @param address   Business address
     * @param businessType  The type of business
     */
    public Business(String name, String description, Address address, BusinessType businessType) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.businessType = businessType;
    }

    /**
     * This method is called when a post request is made through the controller to create a business
     * Created date is generated automatically
     * The logged in user is set as business administrator by default
     */
    public void createBusiness(User owner) {
        this.administrators.add(owner);
        this.primaryAdministrator = owner;
        this.created = new Date();
    }
}
