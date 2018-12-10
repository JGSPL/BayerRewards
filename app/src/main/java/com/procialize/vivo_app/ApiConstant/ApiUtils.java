package com.procialize.vivo_app.ApiConstant;

import com.procialize.vivo_app.RetrofitClient;
import com.procialize.vivo_app.TenorClient;

/**
 * Created by Naushad on 10/30/2017.
 */

public class ApiUtils {

    private ApiUtils() {}


    public static APIService getAPIService() {

        return RetrofitClient.getClient(ApiConstant.baseUrl).create(APIService.class);
    }

    public static TenorApiService getTenorAPIService() {

        return TenorClient.getClient(ApiConstant.tenorUrl).create(TenorApiService.class);
    }
}
