package org.dailykit.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.dailykit.Callback.ScanListener;
import org.dailykit.Model.ScanRequestModel;
import org.dailykit.Model.StatusResponseModel;
import org.dailykit.Retrofit.APIInterface;
import org.dailykit.Retrofit.RetrofitClient;
import org.dailykit.Room.Database.GroctaurantDatabase;
import org.dailykit.Room.Entity.IngredientEntity;
import org.dailykit.Room.Entity.ItemEntity;
import org.dailykit.Util.AppUtil;
import org.dailykit.Util.Constants;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanViewModel extends AndroidViewModel {

    private static final String TAG = "ScanViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    GroctaurantDatabase groctaurantDatabase;
    private APIInterface apiInterface;

    public ScanViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(application, GroctaurantDatabase.class, "Development").allowMainThreadQueries().build();
        apiInterface = RetrofitClient.getClient().getApi();
    }

    public String getIngredientSlipName(ScanListener scanListener,String scanResult){
        String ingredientId=scanResult.substring(0,scanResult.indexOf("_"))+"website"+scanResult.substring(scanResult.indexOf("_"));
        Log.e(TAG,"Ingredient Id :"+ingredientId);
        updateScan(scanListener,ingredientId);
        return groctaurantDatabase.ingredientDao().getSlipNameFromIngredientId(ingredientId);
    }

    public void updateScan(ScanListener scanListener,String ingredientId){
        ScanRequestModel scanRequestModel=new ScanRequestModel(ingredientId,true);
        apiInterface.scanUpdate(scanRequestModel).enqueue(
                new Callback<StatusResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<StatusResponseModel> call, @NotNull Response<StatusResponseModel> response) {
                        if (response.isSuccessful() && response.code() < 300) {
                            if (response.body() != null) {
                                Log.i(TAG,"scanUpdate Response :"+response.body().toString());
                            }
                            groctaurantDatabase.ingredientDao().setScanned(ingredientId,true);
                            setIngredientDetailByItemEntity(scanListener);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<StatusResponseModel> call, @NotNull Throwable t) {
                        Log.e(TAG,"scanUpdate Failure : "+t.toString());

                    }
                }
        );
    }

    public ItemEntity getCurrentItemEntity(){
        return groctaurantDatabase.itemDao().loadItem(sharedpreferences.getString(Constants.SELECTED_ITEM_ID, ""));
    }

    public void setIngredientDetailByItemEntity(ScanListener scanListener){
        ItemEntity itemEntity=getCurrentItemEntity();
        IngredientEntity ingredientEntity=getIngredientById(itemEntity.getItemIngredient().get(itemEntity.getSelectedPosition()).getIngredientId());
        Log.e(TAG,"Ingredient Entity Under Consideration at setIngredientDetailByItemEntity :"+ingredientEntity.toString());
        if(ingredientEntity.isScanned()){
            int leftToPack=groctaurantDatabase.ingredientDao().countIngredientScannedDao(itemEntity.getItemOrderId(),false);
            int nextIndex=0;
            if(leftToPack>0) {
                if(Integer.parseInt(ingredientEntity.getIngredientIndex())>=Integer.parseInt(itemEntity.getItemNoOfIngredient())){
                    nextIndex=1;
                }
                else{
                    nextIndex=itemEntity.getSelectedPosition()+1;
                }
                groctaurantDatabase.itemDao().setSelectedItem(itemEntity.getItemOrderId(), nextIndex);
                itemEntity.setSelectedPosition(nextIndex);
                setIngredientDetailByItemEntity(scanListener);
            }
            else{
                groctaurantDatabase.itemDao().setSelectedItem(itemEntity.getItemOrderId(), -2); // -2 Represents all item packed successfully
                Toast.makeText(getApplication(), "All Ingredient Scanned in this Item", Toast.LENGTH_LONG).show();
            }
        }
        else{
            scanListener.finishActivity();
        }
    }

    public IngredientEntity getIngredientById(String ingredientId){
        return groctaurantDatabase.ingredientDao().getIngredientById(ingredientId);
    }


}
