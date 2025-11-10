package com.api.tests;

import com.api.base.AuthService;
import com.api.base.UserProfileManagementService;
import com.api.models.request.LoginRequest;
import com.api.models.request.UpdateProfileRequest;
import com.api.models.response.LoginResponse;
import com.api.models.response.UserProfileResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateProfileTest {


    @Test(description="verify if profile udpate API is working ")
    public void updateProfileTest(){
        AuthService authService = new AuthService();
        Response response = authService.login(new LoginRequest("shraddha.qa", "Pass@12345"));
        LoginResponse loginResponse =  response.as(LoginResponse.class);
        System.out.println(response.asPrettyString());

        System.out.println("----------Get Token-----------------------------------------------------------");

        UserProfileManagementService userProfileManagementService = new UserProfileManagementService();
        response = userProfileManagementService.getProfile(loginResponse.getToken());
        System.out.println(response.asPrettyString());
        UserProfileResponse userProfileResponse = response.as(UserProfileResponse.class);
        Assert.assertEquals(userProfileResponse.getUsername(), "shraddha.qa", "Incorrect Creds");

        System.out.println("--------Update Profile-----------------------------------------------------------");


        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest.Builder()
                .firstName("Tess")
                .lastName("Joe")
                .email("shraddha.st.qa2@gmail.com")
                .mobileNumber("9876764677")
                .build();


       response = userProfileManagementService.updateProfile(loginResponse.getToken(), updateProfileRequest);
        System.out.println(response.asPrettyString());

    }


}
