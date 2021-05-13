package org.seng302.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InventoryController.class)
@ContextConfiguration(classes = TestApplication.class)
public class InventoryControllerTests {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private ProductController productController;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private InventoryRepository inventoryRepository;
    @Autowired
    private ObjectMapper mapper;

    private User ownerUser;
    private User adminUser;
    private User user;
    private Business business;

    @BeforeEach
    public void setup() throws NoSuchAlgorithmException {
        ownerUser = new User("Rayna", "YEP", "Dalgety", "Universal", "zero tolerance task-force" , "rdalgety3@ocn.ne.jp","2006-03-30","+7 684 622 5902",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ATQWJM");
        ownerUser.setId(1L);
        user = new User("Elwood", "YEP", "Altamirano", "Visionary", "mobile capacity", "ealtamirano8@phpbb.com","1927-02-28","+381 643 240 6530",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"ItqVNvM2JBA");
        user.setId(2L);
        adminUser = new User("Gannon", "YEP", "Tynemouth", "Exclusive", "6th generation intranet", "gtynemouth1@indiatimes.com","1996-03-31","+62 140 282 1784",new Address("32", "Little Fleur Trail", "Christchurch" ,"Canterbury", "New Zealand", "8080"),"HGD0nAJNjSD");
        adminUser.setId(3L);

        Address a1 = new Address("1","Kropf Court","Jequitinhonha", null, "Brazil","39960-000");
        business = new Business("Business1", "Test Business 1", a1, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        business.setId(1L);
        business.createBusiness(ownerUser);
        business.getAdministrators().add(adminUser);
        assertThat(business.getAdministrators().size()).isEqualTo(2);
    }

    @Test
    public void testNoAuthGetInventory() throws Exception {
        mvc.perform(get("/businesses/{id}/inventory", business.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testForbiddenUserGetInventory() throws Exception {
        User forbiddenUser = new User("email@email.com", "password", Role.USER);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, forbiddenUser))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGetInventoryForNullBusiness() throws Exception {
        User user = new User("email@email.com", "password", Role.USER);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(null);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testGlobalAdminGetInventory() throws Exception {
        User defaultGlobalAdmin = new User("email@email.com", "password", Role.DGAA);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, defaultGlobalAdmin))
                .andExpect(status().isOk());

        User globalAdmin = new User("email@email.com", "password", Role.GAA);
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, globalAdmin))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="USER")
    public void testBusinessAdminGetInventory() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser))
                .andExpect(status().isOk());

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(get("/businesses/{id}/inventory", business.getId())
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser))
                .andExpect(status().isOk());
    }
//
//    @Test
//    @WithMockUser(roles="USER")
//    public void testPostProductAsGlobalAdmin() throws Exception {
//        User DGAAUser = new User("email@email.com", "password", Role.DGAA);
//        User GAAUser = new User("email2@email.com", "password", Role.GAA);
//
//        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
//        mvc.perform(post("/businesses/{id}/products", business.getId())
//                .sessionAttr(User.USER_SESSION_ATTRIBUTE, DGAAUser)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(product1)))
//                .andExpect(status().isCreated());
//
//        mvc.perform(post("/businesses/{id}/products", business.getId())
//                .sessionAttr(User.USER_SESSION_ATTRIBUTE, GAAUser)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(product1)))
//                .andExpect(status().isCreated());
//    }
}
