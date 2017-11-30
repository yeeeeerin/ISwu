package com.example.leeyerin.iswu.bases;

import com.example.leeyerin.iswu.model.SpeedStation;
import com.example.leeyerin.iswu.model.SpeedStationInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by MJ on 2017-11-01.
 */

public interface BaseService {

    String host = "http://swopenapi.seoul.go.kr/api/subway/6d754d496a697377313031455a7a4d44/json/shortestRoute/0/5/";

    @Headers("Content-Type: application/json")
    @GET("{firstStation}/{secondStation}")
    Call<SpeedStation> getData(
            @Path("firstStation") String firstStation,
            @Path("secondStation") String secondStation
    );
}
