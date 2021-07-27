package org.seng302.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.seng302.TestApplication;
import org.seng302.finders.BusinessFinder;
import org.seng302.models.Address;
import org.seng302.models.Business;
import org.seng302.models.BusinessType;
import org.seng302.models.User;
import org.seng302.models.requests.BusinessIdRequest;
import org.seng302.models.requests.NewBusinessRequest;
import org.seng302.models.requests.UserIdRequest;
import org.seng302.repositories.BusinessRepository;
import org.seng302.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * Tests relating to the REST Business Controller.
 */
@WebMvcTest(controllers = BusinessController.class)
@ContextConfiguration(classes = TestApplication.class)
class BusinessControllerTests {

    @Autowired
    MockMvc mvc;
    @InjectMocks
    BusinessController businessController;
    @MockBean
    BusinessRepository businessRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private BusinessFinder businessFinder;

    User ownerUser;
    User adminUser;
    User user;
    Business business;

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
    void testNoAuthMakeUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testMakeUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);

        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void makeOwnerAdmin() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        // Attempt to make owner admin.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void makeNullAdmin() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        // Attempt to make owner admin.
        Mockito.when(userRepository.findUserById(10)).thenReturn(null);
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(10)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void makeAdminOfNonExistentBusiness() throws Exception {
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());

        // Test for non-existent business.
        mvc.perform(put("/businesses/{id}/makeAdministrator", 100)
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void makeAdminWithNoneOwnerPrivileges() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        // Non-primary admin user test.
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="GAA") // ownerUser - only for auth purposes.
    void makeAdminWithNoDGAAPrivileges() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        // Non-primary admin user test.
        mvc.perform(put("/businesses/{id}/makeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .content(mapper.writeValueAsString(ownerUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNoAuthRemoveUserAdmin() throws Exception {
        UserIdRequest userIdReq = new UserIdRequest(user.getId());
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userIdReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemoveUserAdmin() throws Exception {
        UserIdRequest adminUserIdReq = new UserIdRequest(adminUser.getId());

        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        Mockito.when(userRepository.findUserById(adminUserIdReq.getUserId())).thenReturn(adminUser);

        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(adminUserIdReq)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemovePrimaryAdminAsAdmin() throws Exception {
        // Attempt to remove primary admin's rights.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemoveNullAsAdmin() throws Exception {
        // Attempt to remove primary admin's rights.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(null);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemoveNonAdminAsAdmin() throws Exception {
        // Attempt to remove primary admin's rights.
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        UserIdRequest userId = new UserIdRequest(user.getId());
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(userId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemoveAdminFromNoneExistingBusiness() throws Exception {
        // Test for non-existent business.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        mvc.perform(put("/businesses/{id}/removeAdministrator", 100)
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isNotAcceptable());
    }


    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testRemoveNonePrimaryAdmin() throws Exception {
        // Non-primary admin user test.
        Mockito.when(userRepository.findUserById(ownerUser.getId())).thenReturn(ownerUser);
        UserIdRequest ownerUserId = new UserIdRequest(ownerUser.getId());
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(put("/businesses/{id}/removeAdministrator", business.getId())
                .contentType("application/json")
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, adminUser)
                .content(mapper.writeValueAsString(ownerUserId)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetBusiness_returnUnauthorized() throws Exception {
        mvc.perform(get("/businesses/{id}", business.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetBusiness_returnNotAcceptable() throws Exception {
        Mockito.when(businessRepository.findBusinessById(99)).thenReturn(null);
        mvc.perform(get("/businesses/{id}", 99))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    void testGetBusiness_returnOk() throws Exception {
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);

        MvcResult result = mvc.perform(get("/businesses/{id}", business.getId()))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(business));

    }

    @Test
    @WithMockUser
    void testCreateBusiness_returnCreated() throws Exception {
        NewBusinessRequest testBusiness = new NewBusinessRequest("Some Business", "Some Description", business.getAddress(), BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);

        mvc.perform(post("/businesses")
                .contentType("application/json")
                .content(mapper.writeValueAsString(testBusiness))
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testInvalidCreateBusiness_returnBadRequest() throws Exception {
        NewBusinessRequest testBusiness = new NewBusinessRequest(null, "Some Description", business.getAddress(), BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        mvc.perform(post("/businesses")
                .contentType("application/json")
                .content(mapper.writeValueAsString(testBusiness))
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void testCreateBusinessWithNullBusinessType() throws Exception  {
        NewBusinessRequest testBusiness = new NewBusinessRequest("Valid Name", "Some Description", business.getAddress(), null);
        mvc.perform(post("/businesses")
                .contentType("application/json")
                .content(mapper.writeValueAsString(testBusiness))
                .sessionAttr(User.USER_SESSION_ATTRIBUTE, user))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testActAsBusinessUserOwns() throws Exception {
        BusinessIdRequest businessIdRequest = new BusinessIdRequest(business.getId());
        Mockito.when(businessRepository.findBusinessById(business.getId())).thenReturn(business);
        mvc.perform(post("/actasbusiness")
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(businessIdRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testActAsNewBusiness() throws Exception {
        BusinessIdRequest businessIdRequest = new BusinessIdRequest(0);
        Mockito.when(businessRepository.findBusinessById(0)).thenReturn(business);
        mvc.perform(post("/actasbusiness")
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(businessIdRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="rdalgety3@ocn.ne.jp", password="ATQWJM", roles="USER") // ownerUser - only for auth purposes.
    void testActAsBusinessDoesntExist() throws Exception {
        BusinessIdRequest businessIdRequest = new BusinessIdRequest(business.getId());
        Mockito.when(businessRepository.findBusinessById(2)).thenReturn(null);
        mvc.perform(post("/actasbusiness")
                .contentType("application/json").sessionAttr(User.USER_SESSION_ATTRIBUTE, ownerUser)
                .content(mapper.writeValueAsString(businessIdRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noSessionBusinessSearch() throws Exception {
        MvcResult results = mvc.perform(get("/businesses/search")
                .param("query", "Pizza"))
                .andReturn();
        assert results.getResponse().getStatus() == HttpStatus.UNAUTHORIZED.value();
    }

    @Test
    @WithMockUser
    void loggedInBusinessSearch() throws Exception {
        MvcResult results = mvc.perform(get("/businesses/search")
                .param("query", "Pizza")
                .param("type", "Retail Trade"))
                .andReturn();
        assert results.getResponse().getStatus() == HttpStatus.OK.value();
    }






}
