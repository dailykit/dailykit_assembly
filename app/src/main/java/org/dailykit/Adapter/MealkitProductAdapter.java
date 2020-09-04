package org.dailykit.adapter;

import android.app.Activity;
import android.util.Log;
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
import org.dailykit.listener.OrderListener;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MealKitProductAdapter extends RecyclerView.Adapter<MealKitProductAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderMealKitProduct> orderMealKitProductList;
    private Activity activity;
    private OrderListener orderListener;
    private MealKitSachetAdapter mealKitSachetAdapter;

    public MealKitProductAdapter(Activity activity, OrderListener orderListener, List<OrderListSubscription.OrderMealKitProduct> orderMealKitProductList) {
        this.orderMealKitProductList = orderMealKitProductList;
        this.orderListener = orderListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_kit_product, parent, false);
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

