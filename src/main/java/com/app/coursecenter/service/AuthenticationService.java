package com.app.coursecenter.service;

import com.app.coursecenter.request.AuthenticationRequest;
import com.app.coursecenter.request.RegisterRequest;
import com.app.coursecenter.response.AuthenticationResponse;

public interface AuthenticationService {

    void register(RegisterRequest input) throws Exception;

    AuthenticationResponse login(AuthenticationRequest request);
}
