package com.tianshaokai.video;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {
    @GET("oldertct.js")
    Call<ResponseBody> getJson();

    @GET("get.php")
    Call<ResponseBody> getVLCPlaylist(@Query("username") String username,
                                      @Query("password") String password,
                                      @Query("type") String type);
}
