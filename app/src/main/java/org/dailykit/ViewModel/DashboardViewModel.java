package org.dailykit.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void fetchOrderList(final DashboardListener dashboardListener){
        /*if(groctaurantDatabase.orderDao().countOrder()>0){
            dashboardListener.updateList();
        }*/
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
                            dashboardListener.updateList();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OrderResponseModel> call, @NotNull Throwable t) {
                        Log.e(TAG,"fetchOrderList Failure : "+t.toString());
                    }
                }
        );
    }

    public void count(){
        Log.e(TAG,"Order Count : "+groctaurantDatabase.orderDao().countOrder());
        Log.e(TAG,"Item Count : "+groctaurantDatabase.itemDao().countItemDao());
        Log.e(TAG,"Ingredient Count : "+groctaurantDatabase.ingredientDao().countIngredientDao());
        Log.e(TAG,"Ingredient Detail Count : "+groctaurantDatabase.ingredientDetailDao().countIngredientDetailDao());
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
                        Log.e(TAG,"repackRequestModel Failure : "+t.toString());

                    }
                }
        );
    }

}
