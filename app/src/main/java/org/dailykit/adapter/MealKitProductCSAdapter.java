package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.listener.OrderListener;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MealKitProductCSAdapter extends RecyclerView.Adapter<MealKitProductCSAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderMealKitProduct> orderMealKitProductList;
    private Activity activity;
    private MealKitProductListener mealKitProductListener;
    private MealKitSachetAdapter mealKitSachetAdapter;

    public MealKitProductCSAdapter(Activity activity, MealKitProductListener mealKitProductListener, List<OrderListSubscription.OrderMealKitProduct> orderMealKitProductList) {
        this.orderMealKitProductList = orderMealKitProductList;
        this.mealKitProductListener = mealKitProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_kit_cs_product, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderMealKitProduct singleItem = orderMealKitProductList.get(position);
        String comboName = "";
        if(null != singleItem.comboProductId() && null != singleItem.comboProduct()){
            comboName = " - "+singleItem.comboProduct().name();
            if(null != singleItem.comboProductComponent()){
                comboName = comboName+" ("+singleItem.comboProductComponent().label()+")";
            }
        }
        try {
            JSONObject yield = new JSONObject(singleItem.simpleRecipeProductOption().simpleRecipeYield().yield().toString());
            holder.serving.setText(yield.get("serving")+"");
        } catch (Throwable t) {
            Timber.e(t.getMessage());
        }
        holder.name.setText(singleItem.simpleRecipeProduct().name()+comboName);
        holder.quantity.setText(singleItem.quantity()+" nos");
        if(singleItem.isAssembled()){
            holder.available.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_green));
        }
        else{
            holder.available.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_grey));
        }

        if(singleItem.orderSachets().size() == 0){
            holder.mealKitSachetList.setVisibility(View.GONE);
        }
        else {
            holder.mealKitSachetList.setVisibility(View.VISIBLE);
            mealKitSachetAdapter = new MealKitSachetAdapter(activity, mealKitProductListener, singleItem.orderSachets());
            holder.mealKitSachetList.setLayoutManager( new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.mealKitSachetList.setAdapter(mealKitSachetAdapter);
            mealKitSachetAdapter.notifyDataSetChanged();
        }
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderMealKitProductList ? orderMealKitProductList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.available)
        ImageView available;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.serving)
        TextView serving;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.meal_kit_sachet_list)
        RecyclerView mealKitSachetList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

