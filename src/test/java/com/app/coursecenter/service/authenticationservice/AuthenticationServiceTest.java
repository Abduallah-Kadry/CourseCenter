package com.app.coursecenter.service.authenticationservice;


import com.app.coursecenter.repository.StudentRepository;
import com.app.coursecenter.request.RegisterRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Fail.fail;







@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Test Class")
public class AuthenticationServiceTest {
//
//    @Autowired
//    StudentRepository studentRepository;
//
//    @Autowired
//    AuthenticationService authenticationService;

    @Test
    void testRegisterStudent() {
//        authenticationService.register(
//                new RegisterRequest("sara", "kadry", "sara@gmail.com", "123sks"));
        fail();
    }

    @Test
    void successMeth(){

    }

    @Disabled
    @Test
    void disapledtest(){

    }

}
