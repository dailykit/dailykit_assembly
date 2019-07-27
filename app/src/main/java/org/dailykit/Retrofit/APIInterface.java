package org.dailykit.Retrofit;

import org.dailykit.Model.OrderResponseModel;
import org.dailykit.Model.RepackRequestModel;
import org.dailykit.Model.ScanRequestModel;
import org.dailykit.Model.StatusResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Danish Rafique on 29-01-2019.
 */
public interface APIInterface {

    @POST("getData")
    Call<OrderResponseModel> fetchOrderList();

    @POST("assembly-update")
    @Headers("Content-Type: application/json")
    Call<StatusResponseModel> scanUpdate(@Body ScanRequestModel scanRequestModel);

    @POST("packing-update")
    @Headers("Content-Type: application/json")
    Call<StatusResponseModel> packingUpdate(@Body RepackRequestModel repackRequestModel);

}
