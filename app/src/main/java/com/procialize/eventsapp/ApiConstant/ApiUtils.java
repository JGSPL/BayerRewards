package com.procialize.eventsapp.ApiConstant;

import com.procialize.eventsapp.RetrofitClient;
import com.procialize.eventsapp.TenorClient;

/**
 * Created by Naushad on 10/30/2017.
 */

public class ApiUtils {

    private ApiUtils() {
    }


    public static APIService getAPIService() {

        return RetrofitClient.getClient(ApiConstant.baseUrl).create(APIService.class);
    }

    public static TenorApiService getTenorAPIService() {

        return TenorClient.getClient(ApiConstant.tenorUrl).create(TenorApiService.class);
    }
}
