package org.dailykit.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.dailykit.OrderListSubscription;
import org.dailykit.listener.DashboardListener;
import org.dailykit.model.IngredientDetailModel;
import org.dailykit.model.IngredientModel;
import org.dailykit.model.ItemModel;
import org.dailykit.model.OrderModel;
import org.dailykit.model.OrderResponseModel;
import org.dailykit.model.RepackRequestModel;
import org.dailykit.model.StatusResponseModel;
import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Danish Rafique on 11-04-2019.
 */
public class DashboardViewModel extends AndroidViewModel {

    private static final String TAG = "DashboardViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    GroctaurantDatabase groctaurantDatabase;
    private APIInterface apiInterface;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(application, GroctaurantDatabase.class, "Development").allowMainThreadQueries().build();
        apiInterface = RetrofitClient.getClient().getApi();

    }

    public void count(){
        Timber.e("Order Count : "+groctaurantDatabase.orderDao().countOrder());
        Timber.e("Item Count : "+groctaurantDatabase.itemDao().countItemDao());
        Timber.e("Ingredient Count : "+groctaurantDatabase.ingredientDao().countIngredientDao());
        Timber.e("Ingredient Detail Count : "+groctaurantDatabase.ingredientDetailDao().countIngredientDetailDao());
    }

    public List<OrderEntity> fetchOrderList(){
        return groctaurantDatabase.orderDao().loadAllOrderList();
    }

    public List<ItemEntity> fetchItemList(){
        return groctaurantDatabase.itemDao().loadItemsByOrderId(sharedpreferences.getString(Constants.SELECTED_ORDER_ID, ""));
    }

    public String getSelectedOrderNumber(){
        return groctaurantDatabase.tabDao().getOrderNumber(sharedpreferences.getString(Constants.SELECTED_ORDER_ID, ""));
    }

    public ItemEntity getCurrentItemEntity(){
        return groctaurantDatabase.itemDao().loadItem(sharedpreferences.getString(Constants.SELECTED_ITEM_ID, ""));
    }

    public List<IngredientEntity> getIngredientEntityList(){
        return groctaurantDatabase.ingredientDao().loadIngredientByItemId(sharedpreferences.getString(Constants.SELECTED_ITEM_ID, ""));
    }

    public void showScanToast(){
        if(!sharedpreferences.getString(Constants.INGREDIENT_FOUND,"").equalsIgnoreCase("")) {
            Toast.makeText(getApplication(), sharedpreferences.getString(Constants.INGREDIENT_FOUND, "").trim(), Toast.LENGTH_LONG).show();
            editor.putString(Constants.INGREDIENT_FOUND, "");
            editor.commit();
        }
    }

    public int getNumberOfIngredientsPacked(){
        return groctaurantDatabase.ingredientDao().countIngredientPacked(sharedpreferences.getString(Constants.SELECTED_ITEM_ID, ""), true);
    }


    public void updatePacking(String ingredientId){
        RepackRequestModel repackRequestModel=new RepackRequestModel(ingredientId,false,false,false);
        apiInterface.packingUpdate(repackRequestModel).enqueue(
                new Callback<StatusResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<StatusResponseModel> call, @NotNull Response<StatusResponseModel> response) {
                        if (response.isSuccessful() && response.code() < 300) {
                            if (response.body() != null) {
                                Log.i(TAG,"repackRequestModel Response :"+response.body().toString());
                            }
                            groctaurantDatabase.ingredientDao().isPackedCompleteUpdate(ingredientId,false);
                            groctaurantDatabase.ingredientDao().isLabeledUpdate(ingredientId,false);
                            groctaurantDatabase.ingredientDetailDao().isPackedUpdateByIngredientId(ingredientId,false);
                            groctaurantDatabase.ingredientDetailDao().isWeighedUpdate(ingredientId,false);
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<StatusResponseModel> call, @NotNull Throwable t) {
                        Timber.e("repackRequestModel Failure : "+t.toString());

                    }
                }
        );
    }

    public void convertOrderResponseToPOJO(DashboardListener dashboardListener,OrderListSubscription.Order orders){
        Gson gson = new Gson();


        /*try {
            JSONArray jsonEvents = new JSONObject(body).getJSONObject("data").getJSONObject("allEvents").getJSONArray("edges");
            for (int i = 0; i < jsonEvents.length(); ++i) {
                JSONObject event = jsonEvents.getJSONObject(i).getJSONObject("node");
                Log.v("TAG", gson.fromJson(event.toString(), Event.class).toString());
            }








        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

}
