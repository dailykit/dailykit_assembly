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
import org.dailykit.listener.OrderListener;
import org.dailykit.util.AppUtil;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.SingleItemRowHolder> {

    private List<OrderListSubscription.Order> allDetailModelArrayList;
    private Activity activity;
    private OrderListener orderListener;
    private InventoryProductAdapter inventoryProductAdapter;
    private ReadyToEatProductAdapter readyToEatProductAdapter;
    private MealKitProductAdapter mealKitProductAdapter;

    public OrderAdapter(Activity activity, OrderListener orderListener, List<OrderListSubscription.Order> allDetailModelArrayList) {
        this.allDetailModelArrayList = allDetailModelArrayList;
        this.orderListener = orderListener;
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
        Timber.e("Ordered On : " + singleItem.created_at().toString().substring(0, 19));
        try {
            Timber.e(singleItem.customer().toString());
            JSONObject customer = new JSONObject(String.valueOf(singleItem.customer()).trim());

            if (null != customer.getString("customerFirstName") && null != customer.getString("customerLastName") && !customer.getString("customerFirstName").isEmpty() && !customer.getString("customerLastName").isEmpty()) {
                holder.customerName.setVisibility(View.VISIBLE);
                holder.customerName.setText(customer.getString("customerFirstName") + " " + customer.getString("customerLastName"));
            } else {
                holder.customerName.setVisibility(View.GONE);
            }
            if (null != customer.getString("customerPhone") && !customer.getString("customerPhone").isEmpty()) {
                holder.customerNumber.setVisibility(View.VISIBLE);
                holder.customerNumber.setText(customer.getString("customerPhone"));
            } else {
                holder.customerNumber.setVisibility(View.GONE);
            }
            if (null != customer.getString("customerEmail") && !customer.getString("customerEmail").isEmpty()) {
                holder.customerEmail.setVisibility(View.VISIBLE);
                holder.customerEmail.setText(customer.getString("customerEmail"));
            } else {
                holder.customerEmail.setVisibility(View.GONE);
            }

            if ((null == customer.getString("customerPhone") || customer.getString("customerPhone").isEmpty()) && (null == customer.getString("customerEmail") || customer.getString("customerEmail").isEmpty())) {
                holder.toggle.setVisibility(View.GONE);
            } else {
                holder.toggle.setVisibility(View.VISIBLE);
            }

        } catch (Throwable t) {
            Timber.e(t.getMessage());
        }

        if (singleItem.orderInventoryProducts().size() == 0) {
            holder.inventoryLayout.setVisibility(View.GONE);
        } else {
            holder.inventoryLayout.setVisibility(View.VISIBLE);
            inventoryProductAdapter = new InventoryProductAdapter(activity, orderListener, singleItem.orderInventoryProducts());
            holder.inventoryList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.inventoryList.setAdapter(inventoryProductAdapter);
            inventoryProductAdapter.notifyDataSetChanged();
        }

        if (singleItem.orderReadyToEatProducts().size() == 0) {
            holder.readyToEatLayout.setVisibility(View.GONE);
        } else {
            holder.readyToEatLayout.setVisibility(View.VISIBLE);
            readyToEatProductAdapter = new ReadyToEatProductAdapter(activity, orderListener, singleItem.orderReadyToEatProducts());
            holder.readyToEatList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.readyToEatList.setAdapter(readyToEatProductAdapter);
            readyToEatProductAdapter.notifyDataSetChanged();
        }

        if (singleItem.orderMealKitProducts().size() == 0) {
            holder.mealKitLayout.setVisibility(View.GONE);
        } else {
            holder.mealKitLayout.setVisibility(View.VISIBLE);
            mealKitProductAdapter = new MealKitProductAdapter(activity, orderListener, singleItem.orderMealKitProducts());
            holder.mealKitList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            holder.mealKitList.setAdapter(mealKitProductAdapter);
            mealKitProductAdapter.notifyDataSetChanged();
        }
        holder.amount.setText(activity.getResources().getString(R.string.dollar) + " 0.00");
        holder.layout.setOnClickListener(v -> {
            orderListener.moveToContinuousScanActivity(singleItem);
        });
        holder.readyToEatLayout.setOnClickListener(v -> {
            orderListener.moveToContinuousScanActivity(singleItem);
        });
        holder.mealKitLayout.setOnClickListener(v -> {
            orderListener.moveToContinuousScanActivity(singleItem);
        });
        holder.inventoryLayout.setOnClickListener(v -> {
            orderListener.moveToContinuousScanActivity(singleItem);
        });

        holder.orderedOn.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", ((String) singleItem.created_at()).substring(0, 18)));
        holder.readyBy.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", (String) singleItem.readyByTimestamp()));
        holder.pickUp.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", (String) singleItem.fulfillmentTimestamp()));

        holder.toggle.setOnClickListener(v -> {
            if (holder.customerDetail.getVisibility() == View.GONE) {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                holder.customerDetail.setVisibility(View.VISIBLE);
            } else {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                holder.customerDetail.setVisibility(View.GONE);
            }
        });

        int count = singleItem.orderInventoryProducts().size()+singleItem.orderMealKitProducts().size()+singleItem.orderReadyToEatProducts().size();
        holder.itemCount.setText("0/"+count);
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != allDetailModelArrayList ? allDetailModelArrayList.size() : 0);
    }

    static
    class SingleItemRowHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_name)
        TextView orderName;
        @BindView(R.id.item_count)
        TextView itemCount;
        @BindView(R.id.customer_name)
        TextView customerName;
        @BindView(R.id.toggle)
        ImageView toggle;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.customer_number)
        TextView customerNumber;
        @BindView(R.id.customer_email)
        TextView customerEmail;
        @BindView(R.id.customer_detail)
        LinearLayout customerDetail;
        @BindView(R.id.ordered_on)
        TextView orderedOn;
        @BindView(R.id.ready_by)
        TextView readyBy;
        @BindView(R.id.pick_up)
        TextView pickUp;
        @BindView(R.id.hidden_layout)
        LinearLayout hiddenLayout;
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
        @BindView(R.id.layout)
        LinearLayout layout;

        SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

