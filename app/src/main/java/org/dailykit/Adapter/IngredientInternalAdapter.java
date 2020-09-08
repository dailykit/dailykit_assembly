package org.dailykit.adapter;

import android.app.Activity;
import androidx.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dailykit.activity.DashboardActivity;
import org.dailykit.R;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.database.GroctaurantExecutor;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;

import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * Created by Danish Rafique on 16-10-2018.
 */
public class IngredientInternalAdapter extends RecyclerView.Adapter<IngredientInternalAdapter.SingleItemRowHolder> {

    public static final String TAG = "IngredientScInternal";
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int currentPosition;
    private IngredientEntity ingredientEntity;
    private DashboardActivity activity;
    private boolean isSelected;
    private IngredientDetailEntity selectedIngredientDetailEntity;
    GroctaurantDatabase groctaurantDatabase;
    GroctaurantExecutor groctaurantExecutor;
    ItemEntity itemEntity;
    int ingredientPosition;

    public IngredientInternalAdapter(Activity activity, IngredientEntity ingredientEntity, boolean isSelected, ItemEntity itemEntity, int ingredientPosition) {
        this.ingredientEntity = ingredientEntity;
        this.activity = (DashboardActivity) activity;
        this.isSelected = isSelected;
        this.sharedpreferences = AppUtil.getAppPreferences(activity);
        this.editor = sharedpreferences.edit();
        Type type = new TypeToken<IngredientDetailEntity>() {}.getType();
        this.selectedIngredientDetailEntity = new Gson().fromJson(sharedpreferences.getString(Constants.SELECTED_INGREDIENT_ENTITY, ""), type);
        groctaurantDatabase = Room.databaseBuilder(activity, GroctaurantDatabase.class, "Development")
                .allowMainThreadQueries()
                .build();
        groctaurantExecutor = GroctaurantExecutor.getInstance();
        this.itemEntity=itemEntity;
        this.ingredientPosition=ingredientPosition;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_element, parent,false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final IngredientDetailEntity singleItem = ingredientEntity.getIngredientEntity().get(position);
        Type type = new TypeToken<IngredientDetailEntity>() {}.getType();
        selectedIngredientDetailEntity = new Gson().fromJson(sharedpreferences.getString(Constants.SELECTED_INGREDIENT_ENTITY, ""), type);
        //holder.ingredientInternalLayout.setMinimumWidth(sharedpreferences.getInt(Constants.WIDTH_OF_INGREDIENT_LIST, 1188));
        if(selectedIngredientDetailEntity.getIngredientDetailId()==null){
            editor.putString(Constants.SELECTED_INGREDIENT_ENTITY, new Gson().toJson(ingredientEntity.getIngredientEntity().get(0)));
            editor.commit();
        }
        Timber.e("Selected : "+selectedIngredientDetailEntity.toString());
        Timber.e("Current : "+singleItem.toString());

        if (isSelected && singleItem.getIngredientDetailId().equals(selectedIngredientDetailEntity.getIngredientDetailId())) {

            holder.ingredientInternalLayout.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.ingredientInternalName.setTextColor(activity.getResources().getColor(R.color.black));
            holder.ingredientInternalWeight.setTextColor(activity.getResources().getColor(R.color.black));
        } else {
            //holder.ingredientInternalLayout.setBackgroundColor(0x00000000);
            holder.ingredientInternalName.setTextColor(activity.getResources().getColor(R.color.white));
            holder.ingredientInternalWeight.setTextColor(activity.getResources().getColor(R.color.white));
        }
        holder.ingredientInternalName.setText(singleItem.getIngredientProcess() + " - " + singleItem.getIngredientName());
        holder.ingredientInternalWeight.setText(singleItem.getIngredientQuantity() + " gms");

        holder.ingredientInternalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.e( singleItem.getIngredientName());
                groctaurantDatabase.ingredientDao().setSelectedItem(ingredientEntity.getIngredientId(), position);
                groctaurantDatabase.itemDao().setSelectedItem(itemEntity.getItemOrderId(), ingredientPosition);
                editor.putString(Constants.SELECTED_INGREDIENT_ENTITY, new Gson().toJson(singleItem));
                editor.commit();
                itemEntity.setSelectedPosition(ingredientPosition);
                Timber.e("ChangeIngredientList Called 4");
                activity.updateIngredientList();
            }
        });
        if (singleItem.isPacked()) {
           // holder.ingredientInternalLayout.setBackgroundColor(activity.getResources().getColor(R.color.black));
        }
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != ingredientEntity.getIngredientEntity() ? ingredientEntity.getIngredientEntity().size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView ingredientInternalName, ingredientInternalWeight;
        protected LinearLayout ingredientInternalLayout;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.ingredientInternalName = (TextView) itemView.findViewById(R.id.ingredient_internal_name);
            this.ingredientInternalWeight = (TextView) itemView.findViewById(R.id.ingredient_internal_weight);
            this.ingredientInternalLayout = (LinearLayout) itemView.findViewById(R.id.ingredient_internal_layout);
        }
    }
}


