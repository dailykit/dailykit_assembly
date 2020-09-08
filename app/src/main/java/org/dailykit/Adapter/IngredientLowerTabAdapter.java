package org.dailykit.adapter;

import android.app.Activity;
import androidx.room.Room;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dailykit.activity.DashboardActivity;
import org.dailykit.R;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.database.GroctaurantExecutor;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;

import java.util.List;

import timber.log.Timber;

public class IngredientLowerTabAdapter extends RecyclerView.Adapter<IngredientLowerTabAdapter.SingleItemRowHolder> {

    public static final String TAG = "IngredientLowerTabAdapt";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int currentPosition;
    GroctaurantDatabase groctaurantDatabase;
    private DashboardActivity activity;
    GroctaurantExecutor groctaurantExecutor;
    private List<ItemEntity> itemEntityList;

    public IngredientLowerTabAdapter(Activity activity, List<ItemEntity> itemEntityList) {
        this.itemEntityList = itemEntityList;
        this.activity = (DashboardActivity)activity;
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(activity, GroctaurantDatabase.class, "Development")
                .allowMainThreadQueries()
                .build();
        groctaurantExecutor = GroctaurantExecutor.getInstance();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_tabs, parent,false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final ItemEntity singleItem = itemEntityList.get(position);
        int activeTab = sharedpreferences.getInt(Constants.ACTIVE_POSITION, 0);

        if (position == activeTab) {
            holder.llTabActive.setVisibility(View.VISIBLE);
            holder.llTabInactive.setVisibility(View.GONE);
            editor.putString(Constants.SELECTED_ITEM_ID, singleItem.getItemOrderId());
            editor.commit();
            //Timber.e("ChangeIngredientList Called 5");
            //activity.changeIngredientList();
        } else {
            holder.llTabActive.setVisibility(View.GONE);
            holder.llTabInactive.setVisibility(View.VISIBLE);
        }

        holder.llTabInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Constants.SELECTED_ITEM_ID, singleItem.getItemOrderId());
                editor.putInt(Constants.ACTIVE_POSITION, position);
                editor.commit();
                updateList();
                Timber.e("ChangeIngredientList Called 7");
                activity.updateIngredientList();
            }
        });

        int numberOfScannedIngredients = groctaurantDatabase.ingredientDao().countIngredientScannedDao(singleItem.getItemOrderId(), true);
        int numberOfPackedIngredients = groctaurantDatabase.ingredientDao().countIngredientPacked(singleItem.getItemOrderId(), true);
        holder.ingredientTabPending.setText(numberOfScannedIngredients + "/"+numberOfPackedIngredients+"/"+ singleItem.getItemNoOfIngredient());
        holder.ingredientTabPendingInactive.setText(numberOfScannedIngredients + "/" +numberOfPackedIngredients+"/"+ singleItem.getItemNoOfIngredient());
        String servingValue[] = singleItem.getItemServing().split(",");
        holder.ingredientTabServing.setText(servingValue[position]);
        holder.ingredientTabRecipeName.setText(singleItem.getItemName());
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != itemEntityList ? itemEntityList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        private LinearLayout llTabActive, llTabInactive;
        private TextView ingredientTabPending, ingredientTabServing, ingredientTabRecipeName, ingredientTabPendingInactive;


        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.llTabActive = (LinearLayout) itemView.findViewById(R.id.ll_tab_active);
            this.llTabInactive = (LinearLayout) itemView.findViewById(R.id.ll_tab_inactive);
            this.ingredientTabPending = (TextView) itemView.findViewById(R.id.ingredient_tab_pending);
            this.ingredientTabPendingInactive = (TextView) itemView.findViewById(R.id.ingredient_tab_pending_inactive);
            this.ingredientTabServing = (TextView) itemView.findViewById(R.id.ingredient_tab_serving);
            this.ingredientTabRecipeName = (TextView) itemView.findViewById(R.id.ingredient_tab_recipe_name);

        }
    }
}
