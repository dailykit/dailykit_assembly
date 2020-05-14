package org.dailykit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.util.AppUtil;
import org.dailykit.util.Constants;

public class RealmViewModel extends AndroidViewModel {

    private static final String TAG = "RealmViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    GroctaurantDatabase groctaurantDatabase;
    private APIInterface apiInterface;

    public RealmViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(application, GroctaurantDatabase.class, "Development").allowMainThreadQueries().build();
        apiInterface = RetrofitClient.getClient().getApi();
    }

    public void setRealm(String realm){
        editor.putString(Constants.REALM_NAME,realm);
        editor.commit();
    }

    public void setClientId(String clientId){
        editor.putString(Constants.CLIENT_ID,clientId);
        editor.commit();
    }

}
