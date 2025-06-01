package com.example.internshipportal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<ResponseMessage> registerUser(@Body RegisterRequest request);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET("internships")
    Call<List<Internship>> getInternships(@Header("Authorization") String token); // if protected

    @POST("/api/internships")
    Call<Internship> addInternship(@Header("Authorization") String token, @Body Internship internship);

}
