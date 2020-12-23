package org.dailykit.retrofit;

import org.dailykit.model.LoginResponseModel;
import org.dailykit.model.OrderResponseModel;
import org.dailykit.model.RepackRequestModel;
import org.dailykit.model.ScanRequestModel;
import org.dailykit.model.StatusResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("getData")
    Call<OrderResponseModel> fetchOrderList();

    @POST("assembly-update")
    @Headers("Content-Type: application/json")
    Call<StatusResponseModel> scanUpdate(@Body ScanRequestModel scanRequestModel);

    @POST("packing-update")
    @Headers("Content-Type: application/json")
    Call<StatusResponseModel> packingUpdate(@Body RepackRequestModel repackRequestModel);

    @POST("auth/realms/{realmName}/protocol/openid-connect/token")
    @FormUrlEncoded
    Call<LoginResponseModel> login(@Path(value = "realmName", encoded = true)String realmName, @FieldMap Map<String,String> fields);

}
