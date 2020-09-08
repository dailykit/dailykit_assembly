package org.dailykit.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.model.ScanIngredientDataModel;
import org.dailykit.model.ScanRequestModel;
import org.dailykit.model.StatusResponseModel;
import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ContinuousScanViewModel extends AndroidViewModel {

    private static final String TAG = "ContinuousScanViewModel";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private APIInterface apiInterface;
    Type type;
    ScanIngredientDataModel scanIngredientDataModel;

    public ContinuousScanViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        apiInterface = RetrofitClient.getClient().getApi();
    }

    public String getItemPackStatus(ItemEntity itemEntity){
        return null;
    }

    public String getIngredientSlipName(ContinuousScanListener continuousScanListener, String scanResult){
        String ingredientId=scanResult.substring(0,scanResult.indexOf("_"))+"website"+scanResult.substring(scanResult.indexOf("_"));
        String ingredientItemId = ingredientId.substring(0,ingredientId.lastIndexOf('_'));
        if(ingredientItemId.equals(getCurrentItemEntity().getItemOrderId())){
            continuousScanListener.setScannedIngredientDetail();
            updateScan(continuousScanListener,ingredientId);
            return null;       }
        else{
            return "Ingredient not in this order";
        }
    }


    public void updateScan(ContinuousScanListener continuousScanListener,String ingredientId){
        ScanRequestModel scanRequestModel=new ScanRequestModel(ingredientId,true);
        apiInterface.scanUpdate(scanRequestModel).enqueue(
                new Callback<StatusResponseModel>() {
                    @Override
                    public void onResponse(@NotNull Call<StatusResponseModel> call, @NotNull Response<StatusResponseModel> response) {
                        if (response.isSuccessful() && response.code() < 300) {
                            if (response.body() != null) {
                                Log.i(TAG,"scanUpdate Response :"+response.body().toString());
                            }
                            //groctaurantDatabase.ingredientDao().setScanned(ingredientId,true);
                            setIngredientDetailByItemEntity(continuousScanListener);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<StatusResponseModel> call, @NotNull Throwable t) {
                        Timber.e("scanUpdate Failure : "+t.toString());

                    }
                }
        );
    }

    public void setIngredientDetailByItemEntity(ContinuousScanListener continuousScanListener){
        ItemEntity itemEntity=getCurrentItemEntity();
        IngredientEntity ingredientEntity;
        if(itemEntity.getSelectedPosition()==-1){
            ingredientEntity=getIngredientById(itemEntity.getItemIngredient().get(0).getIngredientId());
        }
        else{
            ingredientEntity=getIngredientById(itemEntity.getItemIngredient().get(itemEntity.getSelectedPosition()).getIngredientId());
        }

        Timber.e("Ingredient Entity Under Consideration at setIngredientDetailByItemEntity :"+ingredientEntity.toString());
        /*if(ingredientEntity.isScanned()){
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
                setIngredientDetailByItemEntity(continuousScanListener);
            }
            else{
                groctaurantDatabase.itemDao().setSelectedItem(itemEntity.getItemOrderId(), -2); // -2 Represents all item packed successfully
                Toast.makeText(getApplication(), "All Ingredient Scanned in this Item", Toast.LENGTH_LONG).show();
            }
        }
        else{
            continuousScanListener.updateIngredientList();
        }*/
    }

    public IngredientEntity getIngredientById(String ingredientId){
        return null;
    }

    public ItemEntity getCurrentItemEntity(){
        Timber.e(sharedpreferences.getString(Constants.SELECTED_ITEM_ID, ""));
        return null;
    }

    public ScanIngredientDataModel getScanIngredient(){
        type = new TypeToken<ScanIngredientDataModel>() {}.getType();
        scanIngredientDataModel = new Gson().fromJson(sharedpreferences.getString(Constants.SCANNED_INGREDIENT_ENTITY, null), type);
        return scanIngredientDataModel;
    }
}
