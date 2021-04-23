package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.Business;
import org.seng302.models.BusinessType;
import org.seng302.models.User;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;

/**
 * Tests relating to the REST Business Controller.
 */
@WebMvcTest(controllers = BusinessController.class)
@ContextConfiguration(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class BusinessControllerTests {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private BusinessController businessController;
    @MockBean
    private BusinessRepository businessRepository;
    @MockBean
    private UserRepository userRepository;
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
    public void testNoAuthMakeUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    public void testMakeUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);

        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isOk());

        // Attempt to make owner admin.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isBadRequest());

        // Test for non-existent business.
        mvc.perform(put("/businesses/{id}/makeAdministrator", 100)
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isNotAcceptable());

        // Non-primary admin user test.
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testNoAuthRemoveUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    public void testRemoveUserAdmin() throws Exception {
        UserIdRequest adminUserIdReq = new UserIdRequest(adminUser.getId());

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(userRepository.findUserById(adminUserIdReq.getUserId())).thenReturn(adminUser);

        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(adminUserIdReq)))
                .andExpect(status().isOk());

        // Attempt to remove primary admin's rights.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isBadRequest());

        // Test for non-existent business.
        mvc.perform(put("/businesses/{id}/removeAdministrator", 100)
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isNotAcceptable());

        // Non-primary admin user test.
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isForbidden());
    }
}
