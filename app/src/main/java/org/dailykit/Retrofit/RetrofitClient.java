package org.dailykit.retrofit;

import org.dailykit.constants.URLConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Danish Rafique on 29-01-2019.
 */
public class RetrofitClient {

    private static RetrofitClient minstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URLConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized RetrofitClient getClient() {
        if (minstance == null) {
            minstance = new RetrofitClient();

        }
        return minstance;

    }

    public APIInterface getApi() {
        return retrofit.create(APIInterface.class);
    }

}

