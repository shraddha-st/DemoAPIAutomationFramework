package com.api.tests;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.response.LoginResponse;
import com.api.models.response.UserProfileResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class GetProfileRequestTest {

    @Test
    public void getProfileInfoTest(){

        AuthService authService = new AuthService();
        Response response = authService.login(new LoginRequest("shraddha.qa", "Pass@12345"));
        LoginResponse loginResponse = response.as(LoginResponse.class);
        System.out.println(loginResponse.getToken());

        UserProfileManagementService userProfileManangementService =
                new UserProfileManagementService();
//
        response = userProfileManangementService.getProfile(loginResponse.getToken());
//
//
        System.out.println(response.asPrettyString());

        // deserialize json response
        // create userProfleResponse
        UserProfileResponse userProfleResponse = response.as(UserProfileResponse.class);

        System.out.println(userProfleResponse.getUsername());



    }
}
