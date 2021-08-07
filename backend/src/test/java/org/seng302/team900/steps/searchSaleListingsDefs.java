package org.seng302.team900.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.CucumberRunnerTest;
import org.seng302.controllers.ListingController;
import org.seng302.finders.ListingSpecifications;
import org.seng302.models.*;
import org.seng302.models.requests.BusinessListingSearchRequest;
import org.seng302.repositories.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = CucumberRunnerTest.class)
@WebMvcTest(controllers = ListingController.class)
public class searchSaleListingsDefs {

    private MockMvc mockMvc;

    @InjectMocks
    private ListingController listingController;

    @MockBean
    private ListingRepository listingRepository;

    @MockBean
    private ListingSpecifications listingSpecifications;

    private ObjectMapper mapper;

    private Page<Listing> pagedListings;
    private MockHttpServletResponse response;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        listingRepository = Mockito.mock(ListingRepository.class);
        listingSpecifications = Mockito.mock(ListingSpecifications.class);

        this.mockMvc = MockMvcBuilders.standaloneSetup(new ListingController(listingRepository, mapper)).build();
    }

    @Given("there are sale listings available")
    public void thereAreSaleListingsAvailable() {
        Calendar calendar = Calendar.getInstance();
        Date dateCreated = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        Date dateCloses = calendar.getTime();

        Inventory inventory = new Inventory("77-9986231", 1, 20, 2.0, 10.0, dateCreated,dateCloses, dateCloses, dateCloses);
        Listing listing1 = new Listing(inventory, 12, 12.00, "test more info", dateCreated, dateCloses);
        Listing listing2 = new Listing(inventory, 8, 10.00, "test more info", dateCreated, dateCloses);
        List<Listing> listingList = new ArrayList<>();
        listingList.add(listing1);
        listingList.add(listing2);
        pagedListings = new PageImpl<>(listingList);
        Mockito.when(listingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedListings);

        Page<Listing> results = listingRepository.findAll(where(listingSpecifications.hasPriceSet()).and(listingSpecifications.hasClosingDateSet()), Pageable.unpaged());

        assertThat(results.getTotalElements()).isEqualTo(pagedListings.getTotalElements());
    }

    @When("the user makes a request to look at current listings")
    public void theUserMakesARequestToLookAtCurrentListings() throws Exception {
        BusinessListingSearchRequest request = new BusinessListingSearchRequest();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("count", "5");
        requestParams.add("offset", "1");
        requestParams.add("sortDirection", "asc");

        response = mockMvc.perform(post("/businesses/listings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .params(requestParams)
                            .content(mapper.writeValueAsString(request))).andReturn().getResponse();

        assertThat(response).isNotNull();
    }


    @Then("the user successfully retrieves them")
    public void theUserSuccessfullyRetrievesThem() throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotNull();
    }


}
