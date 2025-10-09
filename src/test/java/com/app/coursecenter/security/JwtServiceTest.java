package com.app.coursecenter.security;


import com.app.coursecenter.service.authenticationservice.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class JwtServiceTest {
    // test token generation with user details
    // need to setup dependencides of the jwt which is (user detials)
    // the jwt runs on api so you need to enable the web mvc

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    // test that student with not auth shouldn't do anythingc

    @Test
    void publicEndPoint_withoutAuth_shouldBeAllowed() throws Exception {
        mockMvc.perform(
                post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(
                        """
                                {
                                "email": "test@example.com",
                                "password":"password"
                                }
                                """
                )).andExpect(status().isUnauthorized());

    }


}
