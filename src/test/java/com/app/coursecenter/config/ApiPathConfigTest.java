package com.app.coursecenter.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiPathConfigTest {


    @InjectMocks
    ApiPathConfig pathConfig;

    @Test
    void getAllPublicPaths() {
        System.out.println(Arrays.toString(pathConfig.getAllPublicPaths()));
    }
}