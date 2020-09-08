package org.dailykit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import com.google.gson.Gson;

import org.dailykit.listener.LoginListener;
import org.dailykit.model.LoginResponseModel;
import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;
import org.dailykit.constants.ErrorConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    GroctaurantDatabase groctaurantDatabase;
    private APIInterface apiInterface;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(application, GroctaurantDatabase.class, "Development").allowMainThreadQueries().build();
        apiInterface = RetrofitClient.getClient().getApi();
    }

    public void login(LoginListener loginListener, String username,String password){
        loginListener.showDialog();
        Map<String,String> fields = new HashMap<>();
        fields.put("grant_type","password");
        fields.put("client_id",sharedpreferences.getString(Constants.CLIENT_ID,""));
        fields.put("username",username);
        fields.put("password",password);
        fields.put("scope","openid");
        Timber.e(fields.toString());
        Timber.e(sharedpreferences.getString(Constants.REALM_NAME,""));
        apiInterface.login(sharedpreferences.getString(Constants.REALM_NAME,""),fields).enqueue(
                new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<LoginResponseModel> call, @NotNull Response<LoginResponseModel> response) {
                        Timber.e("Login Response Code :"+response.code());
                        if (response.isSuccessful() && response.code() < 300) {
                            if (null != response.body()) {
                                Timber.e("Login Response :"+response.body().toString());
                            }
                            editor.putString(Constants.USER_NAME,username);
                            editor.putBoolean(Constants.IS_USER_LOGGED,true);
                            editor.putString(Constants.USER_ACCESS_MODEL,new Gson().toJson(response.body()));
                            editor.commit();
                            loginListener.moveToDashboard();
                        }
                        else if(response.code() == 401){
                            Timber.e( ErrorConstants.AUTHENTICATION_ERROR);
                            Toast.makeText(getApplication(), ErrorConstants.AUTHENTICATION_ERROR, Toast.LENGTH_SHORT).show();
                            loginListener.dismissDialog();
                        }
                        else if(response.code() == 404){
                            Timber.e( ErrorConstants.NOT_FOUND);
                            Toast.makeText(getApplication(), ErrorConstants.NOT_FOUND, Toast.LENGTH_SHORT).show();
                            loginListener.dismissDialog();
                        }
                        else if(response.code() == 504){
                            Timber.e(ErrorConstants.TIMEOUT_MESSAGE);
                            Toast.makeText(getApplication(), ErrorConstants.TIMEOUT_MESSAGE, Toast.LENGTH_SHORT).show();
                            loginListener.dismissDialog();
                        }
                        else{
                            Timber.e( ErrorConstants.FAILURE_MESSAGE);
                            Toast.makeText(getApplication(), ErrorConstants.FAILURE_MESSAGE, Toast.LENGTH_SHORT).show();
                            loginListener.dismissDialog();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<LoginResponseModel> call, @NotNull Throwable t) {
                        Timber.e("Login Failure : "+t.toString());
                        Toast.makeText(getApplication(), ErrorConstants.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                        loginListener.dismissDialog();
                    }
                }
        );
    }



}
