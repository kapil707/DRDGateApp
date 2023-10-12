package com.drd.drdgateapp;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("drd_master_api/api01/test")
    Call<ResponseBody> testing(@Field("test") String test);

    @FormUrlEncoded
    @POST("drd_master_api/api01/get_qr_code_api")
    Call<ResponseBody> get_qr_code_api(@Field("api_key") String api_key);
}
