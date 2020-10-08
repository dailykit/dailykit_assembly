package org.dailykit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.UpdateOrderInventoryProductMutation;
import org.dailykit.UpdateOrderMealKitProductMutation;
import org.dailykit.UpdateOrderMealKitSachetMutation;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.network.Network;
import org.dailykit.type.Order_orderInventoryProduct_pk_columns_input;
import org.dailykit.type.Order_orderInventoryProduct_set_input;
import org.dailykit.type.Order_orderMealKitProduct_pk_columns_input;
import org.dailykit.type.Order_orderMealKitProduct_set_input;
import org.dailykit.type.Order_orderSachet_pk_columns_input;
import org.dailykit.type.Order_orderSachet_set_input;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MealKitSachetODAdapter extends RecyclerView.Adapter<MealKitSachetODAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderSachet> orderSachetList;
    private Activity activity;
    private MealKitProductListener mealKitProductListener;

    public MealKitSachetODAdapter(Activity activity, MealKitProductListener mealKitProductListener, List<OrderListSubscription.OrderSachet> orderSachetList) {
        this.orderSachetList = orderSachetList;
        this.mealKitProductListener = mealKitProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_kit_product_od, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderSachet singleItem = orderSachetList.get(position);
        holder.name.setText(singleItem.ingredientName());
        double unitSize = (null == singleItem.quantity()) ? 0L : (double) singleItem.quantity();
        String unit = (null == singleItem.unit()) ? "" : singleItem.unit();
        holder.serving.setText(unitSize + " " + unit);
        holder.processing.setText(singleItem.processingName());
        if ("PENDING".equals(singleItem.status())) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_yellow));
            holder.status.setText("0/0/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
        } else if ("PACKED".equals(singleItem.status()) && !singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_blue));
            holder.status.setText("0/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));

        } else if ("PACKED".equals(singleItem.status()) && singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_green));
            holder.status.setText("1/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
        }
        holder.toggle.setOnClickListener(v -> {
            if (holder.optionsLayout.getVisibility() == View.GONE) {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                holder.optionsLayout.setVisibility(View.VISIBLE);
            } else {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                holder.optionsLayout.setVisibility(View.GONE);
            }
        });

        holder.markAsAssembled.setOnClickListener(v -> {
            if ("PENDING".equals(singleItem.status())) {
                Toast.makeText(activity, "Please pack this item before assembling", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateOrderMealKitSachetMutation updateOrderMealKitSachetMutation = new UpdateOrderMealKitSachetMutation(
                        Order_orderSachet_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),
                        Order_orderSachet_set_input
                                .builder()
                                .isAssembled(true)
                                .build());

                Network.apolloClient.mutate(updateOrderMealKitSachetMutation).enqueue(new ApolloCall.Callback<UpdateOrderMealKitSachetMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderMealKitSachetMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
            }
        });
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderSachetList ? orderSachetList.size() : 0);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.serving)
        TextView serving;
        @BindView(R.id.processing)
        TextView processing;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.toggle)
        ImageView toggle;
        @BindView(R.id.mark_as_assembled)
        LinearLayout markAsAssembled;
        @BindView(R.id.options_layout)
        LinearLayout optionsLayout;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
