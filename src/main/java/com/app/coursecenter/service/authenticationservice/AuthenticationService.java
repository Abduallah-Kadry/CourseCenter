package com.app.coursecenter.service.authenticationservice;

import com.app.coursecenter.request.AuthenticationRequest;
import com.app.coursecenter.request.RegisterRequest;
import com.app.coursecenter.response.AuthenticationResponse;

public interface AuthenticationService {

    void register(RegisterRequest input);

    AuthenticationResponse login(AuthenticationRequest request);
}
