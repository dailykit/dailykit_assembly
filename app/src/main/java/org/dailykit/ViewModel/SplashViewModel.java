package org.dailykit.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import org.dailykit.Callback.SplashListener;
import org.dailykit.Model.IngredientDetailModel;
import org.dailykit.Model.IngredientModel;
import org.dailykit.Model.ItemModel;
import org.dailykit.Model.OrderModel;
import org.dailykit.Model.OrderResponseModel;
import org.dailykit.Retrofit.APIInterface;
import org.dailykit.Retrofit.RetrofitClient;
import org.dailykit.Room.Database.GroctaurantDatabase;
import org.dailykit.Room.Entity.IngredientDetailEntity;
import org.dailykit.Room.Entity.IngredientEntity;
import org.dailykit.Room.Entity.ItemEntity;
import org.dailykit.Room.Entity.OrderEntity;
import org.dailykit.Util.AppUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void fetchOrderList(final SplashListener splashListener){
        apiInterface.fetchOrderList().enqueue(
                new Callback<OrderResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<OrderResponseModel> call, @NotNull Response<OrderResponseModel> response) {
                        if (response.isSuccessful() && response.code() < 300) {
                            if (response.body() != null) {
                                Log.e(TAG,"fetchOrderList Response :"+response.body().toString());
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
                        Log.e(TAG,"fetchOrderList Failure : "+t.toString());
                    }
                }
        );
    }
}
