package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.listener.OrderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        if(singleItem.orderSachets().size() == 0){
            holder.mealKitSachetList.setVisibility(View.GONE);
        }
        else {
            holder.mealKitSachetList.setVisibility(View.VISIBLE);
            mealKitSachetAdapter = new MealKitSachetAdapter(activity, orderListener, singleItem.orderSachets());
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.meal_kit_sachet_list)
        RecyclerView mealKitSachetList;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

