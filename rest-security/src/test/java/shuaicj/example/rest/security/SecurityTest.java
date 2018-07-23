package shuaicj.example.rest.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import shuaicj.example.rest.security.config.JwtAuthenticationConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test Spring Security.
 *
 * @author shuaicj 2017/08/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtAuthenticationConfig jwtConfig;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void mockUserViaAnnotation() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
    }

    @Test
    public void mockUserViaMethod() throws Exception {
        mockMvc.perform(get("/admin").with(user("admin").password("admin").roles("ADMIN", "USER")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
    }

    @Test
    public void accessAsUnauth() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().isOk());
        mockMvc.perform(get("/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/admin")).andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "shuaicj", roles = "USER")
    public void accessAsUser() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().isOk());
        mockMvc.perform(get("/me")).andExpect(status().isOk());
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden()).andExpect(authenticated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void accessAsAdmin() throws Exception {
        mockMvc.perform(get("/hello")).andExpect(status().isOk());
        mockMvc.perform(get("/me")).andExpect(status().isOk());
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    public void accessByTokenAsUser() throws Exception {
        String token = mockMvc.perform(post(jwtConfig.getUrl())
                .content("{\"username\":\"shuaicj\",\"password\":\"shuaicj\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getHeader("Authorization");
        mockMvc.perform(get("/hello").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("shuaicj").withRoles("USER"));
        mockMvc.perform(get("/me").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("shuaicj").withRoles("USER"));
        mockMvc.perform(get("/admin").header("Authorization", token))
                .andExpect(status().isForbidden())
                .andExpect(authenticated().withUsername("shuaicj").withRoles("USER"));
    }

    @Test
    public void accessByTokenAsAdmin() throws Exception {
        String token = mockMvc.perform(post(jwtConfig.getUrl())
                .content("{\"username\":\"admin\",\"password\":\"admin\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getHeader("Authorization");
        mockMvc.perform(get("/hello").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
        mockMvc.perform(get("/me").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
        mockMvc.perform(get("/admin").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));
    }

    // @Test
    // public void signout() throws Exception {
    //     mockMvc.perform(logout()).andExpect(status().isFound()).andExpect(unauthenticated());
    // }
    //
    // @Test
    // public void httpBasicAuth() throws Exception {
    //     mockMvc.perform(get("/me").with(httpBasic("shuaicj", "shuaicj")))
    //             .andExpect(status().isOk()).andExpect(authenticated().withUsername("shuaicj"));
    // }
    //
    // @Test
    // public void session() throws Exception {
    //     MvcResult result = mockMvc.perform(get("/me").with(httpBasic("shuaicj", "shuaicj")))
    //             .andExpect(status().isOk()).andExpect(authenticated()).andReturn();
    //     MockHttpSession sess = (MockHttpSession) result.getRequest().getSession(false);
    //     mockMvc.perform(get("/me").session(sess)).andExpect(status().isOk()).andExpect(authenticated());
    //     mockMvc.perform(post("/logout").with(csrf()).session(sess)).andExpect(unauthenticated());
    //     mockMvc.perform(get("/me")).andExpect(status().isUnauthorized()).andExpect(unauthenticated());
    // }
}