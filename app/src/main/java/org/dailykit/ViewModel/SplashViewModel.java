package org.dailykit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import org.dailykit.listener.SplashListener;
import org.dailykit.model.IngredientDetailModel;
import org.dailykit.model.IngredientModel;
import org.dailykit.model.ItemModel;
import org.dailykit.model.OrderModel;
import org.dailykit.model.OrderResponseModel;
import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashViewModel extends AndroidViewModel {

    private static final String TAG = "SplashViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    GroctaurantDatabase groctaurantDatabase;
    private APIInterface apiInterface;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(application, GroctaurantDatabase.class, "Development").allowMainThreadQueries().build();
        apiInterface = RetrofitClient.getClient().getApi();
    }

    public boolean isUserLoggedIn(){
        return sharedpreferences.getBoolean(Constants.IS_USER_LOGGED,false);
    }

    public void fetchOrderList(final SplashListener splashListener){
        apiInterface.fetchOrderList().enqueue(
                new Callback<OrderResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<OrderResponseModel> call, @NotNull Response<OrderResponseModel> response) {
                        if (response.isSuccessful() && response.code() < 300) {
                            if (response.body() != null) {
                                Timber.e("fetchOrderList Response :"+response.body().toString());
                                OrderResponseModel orderResponseModel=response.body();
                                for(OrderModel orderModel:orderResponseModel.getAllOrders()){
                                    OrderEntity orderEntity=new OrderEntity(orderModel);
                                    ArrayList<ItemEntity> itemEntityArrayList=new ArrayList<>();
                                    for(ItemModel itemModel:orderModel.getItems()){
                                        ItemEntity itemEntity=new ItemEntity(itemModel);
                                        ArrayList<IngredientEntity> ingredientModelArrayList=new ArrayList<>();
                                        for(IngredientModel ingredientModel:itemModel.getIngredients()){
                                            IngredientEntity ingredientEntity=new IngredientEntity(ingredientModel);
                                            ArrayList<IngredientDetailEntity> ingredientDetailModelArrayList=new ArrayList<>();
                                            for(IngredientDetailModel ingredientDetailModel:ingredientModel.getIngredientDetails()){
                                                IngredientDetailEntity ingredientDetailEntity=new IngredientDetailEntity(ingredientDetailModel);
                                                groctaurantDatabase.ingredientDetailDao().insert(ingredientDetailEntity);
                                                ingredientDetailModelArrayList.add(ingredientDetailEntity);
                                            }
                                            ingredientEntity.setIngredientEntity(ingredientDetailModelArrayList);
                                            groctaurantDatabase.ingredientDao().insert(ingredientEntity);
                                            ingredientModelArrayList.add(ingredientEntity);
                                        }
                                        itemEntity.setItemIngredient(ingredientModelArrayList);
                                        groctaurantDatabase.itemDao().insert(itemEntity);
                                        itemEntityArrayList.add(itemEntity);
                                    }
                                    orderEntity.setOrderItems(itemEntityArrayList);
                                    groctaurantDatabase.orderDao().insert(orderEntity);
                                }
                            }
                            splashListener.moveToNextScreen();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OrderResponseModel> call, @NotNull Throwable t) {
                        Timber.e("fetchOrderList Failure : "+t.toString());
                    }
                }
        );
    }
}
