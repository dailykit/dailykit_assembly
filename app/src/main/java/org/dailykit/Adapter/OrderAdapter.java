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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.SingleItemRowHolder> {

    private List<OrderListSubscription.Order> allDetailModelArrayList;
    private Activity activity;
    private OrderListener orderListener;
    private InventoryProductAdapter inventoryProductAdapter;
    private ReadyToEatProductAdapter readyToEatProductAdapter;
    private MealKitProductAdapter mealKitProductAdapter;

    public OrderAdapter(Activity activity, OrderListener orderListener,List<OrderListSubscription.Order> allDetailModelArrayList) {
        this.allDetailModelArrayList = allDetailModelArrayList;
        this.orderListener =  orderListener;
        this.activity = activity;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final OrderListSubscription.Order singleItem = allDetailModelArrayList.get(position);

        holder.orderName.setText((String) singleItem.id());
        if(singleItem.orderInventoryProducts().size() == 0){
            holder.inventoryLayout.setVisibility(View.GONE);
        }
        else {
            holder.inventoryLayout.setVisibility(View.VISIBLE);
            inventoryProductAdapter = new InventoryProductAdapter(activity, orderListener, singleItem.orderInventoryProducts());
            holder.inventoryList.setLayoutManager( new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.inventoryList.setAdapter(inventoryProductAdapter);
            inventoryProductAdapter.notifyDataSetChanged();
        }

        if(singleItem.orderReadyToEatProducts().size() == 0){
            holder.readyToEatLayout.setVisibility(View.GONE);
        }
        else {
            holder.readyToEatLayout.setVisibility(View.VISIBLE);
            readyToEatProductAdapter = new ReadyToEatProductAdapter(activity, orderListener, singleItem.orderReadyToEatProducts());
            holder.readyToEatList.setLayoutManager( new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.readyToEatList.setAdapter(readyToEatProductAdapter);
            readyToEatProductAdapter.notifyDataSetChanged();
        }

        if(singleItem.orderMealKitProducts().size() == 0){
            holder.mealKitLayout.setVisibility(View.GONE);
        }
        else {
            holder.mealKitLayout.setVisibility(View.VISIBLE);
            mealKitProductAdapter = new MealKitProductAdapter(activity, orderListener, singleItem.orderMealKitProducts());
            holder.mealKitList.setLayoutManager( new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.mealKitList.setAdapter(mealKitProductAdapter);
            mealKitProductAdapter.notifyDataSetChanged();
        }
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != allDetailModelArrayList ? allDetailModelArrayList.size() : 0);
    }

    static
    class SingleItemRowHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.order_name)
        TextView orderName;
        @BindView(R.id.inventory_list)
        RecyclerView inventoryList;
        @BindView(R.id.inventory_layout)
        LinearLayout inventoryLayout;
        @BindView(R.id.ready_to_eat_list)
        RecyclerView readyToEatList;
        @BindView(R.id.ready_to_eat_layout)
        LinearLayout readyToEatLayout;
        @BindView(R.id.meal_kit_list)
        RecyclerView mealKitList;
        @BindView(R.id.meal_kit_layout)
        LinearLayout mealKitLayout;

        SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

