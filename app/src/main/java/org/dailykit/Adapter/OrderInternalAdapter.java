package org.dailykit.adapter;

import android.app.Activity;
import androidx.room.Room;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.dailykit.activity.DashboardActivity;
import org.dailykit.customview.FuturaMediumTextView;
import org.dailykit.customview.ScrollViewText;
import org.dailykit.R;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.room.entity.TabEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.constants.Constants;

import java.util.List;

public class OrderInternalAdapter extends RecyclerView.Adapter<OrderInternalAdapter.SingleItemRowHolder> {

    public static final String TAG = "OrderInternalAdapter";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int currentPosition;

    private DashboardActivity activity;
    GroctaurantDatabase groctaurantDatabase;
    private List<ItemEntity> itemEntityList;
    private String orderId;
    private String currentSelectedItem;

    public OrderInternalAdapter(Activity activity, List<ItemEntity> itemEntityList, int currentPosition, String orderId) {

        this.activity = (DashboardActivity)activity;
        this.currentPosition = currentPosition;
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(activity, GroctaurantDatabase.class, "Development")
                .allowMainThreadQueries()
                .build();
        this.itemEntityList = itemEntityList;
        this.orderId = orderId;
        this.currentSelectedItem=sharedpreferences.getString(Constants.SELECTED_ITEM_ID,"");
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent,false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final ItemEntity singleItem = itemEntityList.get(position);
        if(singleItem.getItemName().length()>30){
            holder.itemOrderNameMarquee.setText(singleItem.getItemName());
            holder.itemOrderNameMarquee.startScroll();
            holder.itemOrderNameMarquee.setVisibility(View.VISIBLE);
            holder.itemOrderName.setVisibility(View.GONE);
        }
        else{
            holder.itemOrderName.setText(singleItem.getItemName());
            holder.itemOrderName.setVisibility(View.VISIBLE);
            holder.itemOrderNameMarquee.setVisibility(View.GONE);
        }
        String servingValue[] = singleItem.getItemServing().split(",");
        holder.itemOrderServing.setText(servingValue[position].trim());
        int numberOfScannedIngredients = groctaurantDatabase.ingredientDao().countIngredientScannedDao(singleItem.getItemOrderId(), true);
        int numberOfPackedIngredients = groctaurantDatabase.ingredientDao().countIngredientPacked(singleItem.getItemOrderId(), true);
        holder.itemOrderPending.setText(numberOfScannedIngredients+"/"+numberOfPackedIngredients+"/"+singleItem.getItemNoOfIngredient());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(25,10,25,10);
        params.height = 120;
        holder.itemOrderLayout.setLayoutParams(params);
        LinearLayout.LayoutParams paramOption = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        paramOption.weight = 1.0f;

        if(numberOfScannedIngredients== Integer.parseInt(singleItem.getItemNoOfIngredient())){
            holder.itemOrderLayout.setBackgroundColor(activity.getResources().getColor(R.color.black));
            holder.orderAlphaMainImage.setVisibility(View.GONE);
        }
        else{
            holder.itemOrderLayout.setBackgroundColor(0x00000000);
            holder.orderAlphaMainImage.setVisibility(View.VISIBLE);
        }

        holder.itemOrderOption.setLayoutParams(paramOption);
        holder.itemOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabEntity tabEntity = new TabEntity(singleItem.getOrderId(), itemEntityList.get(0).getItemNumber(), position);
                editor.putInt(Constants.ACTIVE_POSITION, position);
                IngredientDetailEntity ingredientDetailEntity = new IngredientDetailEntity();
                ingredientDetailEntity.setIngredientName("");
                ingredientDetailEntity.setIngredientQuantity(0);
                editor.putString(Constants.SELECTED_ITEM_ID, singleItem.getItemOrderId());
                editor.putString(Constants.SELECTED_INGREDIENT_ENTITY, new Gson().toJson(ingredientDetailEntity));
                editor.putString(Constants.SELECTED_ORDER_ID, orderId);
                editor.commit();
                groctaurantDatabase.tabDao().insert(tabEntity);
                activity.switchToIngredientFragment();
                /*Intent intent = new Intent(activity, IngredientActivity.class);
                activity.startActivity(intent);*/
            }
        });

        //Timber.e("Id :"+sharedpreferences.getString(Constants.SELECTED_ITEM_ID,"")+" "+singleItem.getItemOrderId());

        if(currentSelectedItem.equals(singleItem.getItemOrderId())){
            holder.imgOnline.setVisibility(View.VISIBLE);
        }
        else{
            holder.imgOnline.setVisibility(View.GONE);
        }


    }

    public void updateList() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != itemEntityList ? itemEntityList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected FuturaMediumTextView itemOrderServing, itemOrderPending,itemOrderName;
        protected ScrollViewText itemOrderNameMarquee;
        protected LinearLayout itemOrderLayout;
        protected LinearLayout itemOrderOption;
        protected ImageView orderAlphaMainImage,imgOnline;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.itemOrderServing = itemView.findViewById(R.id.item_order_serving);
            this.itemOrderPending =  itemView.findViewById(R.id.item_order_pending);
            this.itemOrderName =  itemView.findViewById(R.id.item_order_name);
            this.itemOrderNameMarquee =  itemView.findViewById(R.id.item_order_name_marquee);
            this.itemOrderLayout = (LinearLayout) itemView.findViewById(R.id.item_order_layout);
            this.itemOrderOption = (LinearLayout) itemView.findViewById(R.id.ll_options);
            this.orderAlphaMainImage = (ImageView) itemView.findViewById(R.id.order_alpha_main_image);
            this.imgOnline = (ImageView) itemView.findViewById(R.id.img_online);
        }
    }
}
