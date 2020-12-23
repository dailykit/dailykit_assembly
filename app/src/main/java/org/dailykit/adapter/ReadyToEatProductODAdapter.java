package org.dailykit.adapter;

import android.app.Activity;
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

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.R;
import org.dailykit.UpdateOrderReadyToEatProductMutation;
import org.dailykit.listener.ReadyToEatProductListener;
import org.dailykit.network.Network;
import org.dailykit.type.Order_orderReadyToEatProduct_pk_columns_input;
import org.dailykit.type.Order_orderReadyToEatProduct_set_input;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ReadyToEatProductODAdapter extends RecyclerView.Adapter<ReadyToEatProductODAdapter.ViewHolder> {

    private List<OrderListDetailSubscription.OrderReadyToEatProduct> orderReadyToEatProducts;
    private Activity activity;
    private ReadyToEatProductListener readyToEatProductListener;

    public ReadyToEatProductODAdapter(Activity activity, ReadyToEatProductListener readyToEatProductListener, List<OrderListDetailSubscription.OrderReadyToEatProduct> orderReadyToEatProducts) {
        this.orderReadyToEatProducts = orderReadyToEatProducts;
        this.readyToEatProductListener = readyToEatProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ready_to_eat_product_od, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListDetailSubscription.OrderReadyToEatProduct singleItem = orderReadyToEatProducts.get(position);
        String comboName = "";
        if (null != singleItem.comboProductId() && null != singleItem.comboProduct()) {
            comboName = " - " + singleItem.comboProduct().name();
            if (null != singleItem.comboProductComponent()) {
                comboName = comboName + " (" + singleItem.comboProductComponent().label() + ")";
            }
        }
        try {
            JSONObject yield = new JSONObject(singleItem.simpleRecipeProductOption().simpleRecipeYield().yield().toString());
            holder.serving.setText(yield.get("serving") + " Servings");
        } catch (Throwable t) {
            Timber.e(t.getMessage());
        }
        holder.name.setText(singleItem.simpleRecipeProduct().name() + comboName);
        if ("PENDING".equals(singleItem.assemblyStatus())) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_yellow));
            holder.status.setText("0/0/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && !singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_blue));
            holder.status.setText("0/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_green));
            holder.status.setText("1/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
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
            if ("PENDING".equals(singleItem.assemblyStatus())) {
                Toast.makeText(activity, "Please pack this item before assembling", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateOrderReadyToEatProductMutation updateOrderReadyToEatProductMutation = new UpdateOrderReadyToEatProductMutation(
                        Order_orderReadyToEatProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),

                        Order_orderReadyToEatProduct_set_input
                                .builder()
                                .isAssembled(true)
                                .build());

                Network.apolloClient.mutate(updateOrderReadyToEatProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderReadyToEatProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderReadyToEatProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        readyToEatProductListener.onResponse(singleItem,"Marked Assembled Successfully");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
            }

        });

        holder.repack.setOnClickListener(v -> {
            if ("PENDING".equals(singleItem.assemblyStatus())) {
                Toast.makeText(activity, "Please pack this item before repacking", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateOrderReadyToEatProductMutation updateOrderReadyToEatProductMutation = new UpdateOrderReadyToEatProductMutation(
                        Order_orderReadyToEatProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),

                        Order_orderReadyToEatProduct_set_input
                                .builder()
                                .assemblyStatus("PENDING")
                                .build());

                Network.apolloClient.mutate(updateOrderReadyToEatProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderReadyToEatProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderReadyToEatProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        readyToEatProductListener.onResponse(singleItem,"Repacked Successfully");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
            }

        });

        holder.reassemble.setOnClickListener(v -> {
            if (!("COMPLETED".equals(singleItem.assemblyStatus()) && singleItem.isAssembled())) {
                Toast.makeText(activity, "Please assemble this item before reassembling", Toast.LENGTH_SHORT).show();
            }
            else {
                UpdateOrderReadyToEatProductMutation updateOrderReadyToEatProductMutation = new UpdateOrderReadyToEatProductMutation(
                        Order_orderReadyToEatProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),

                        Order_orderReadyToEatProduct_set_input
                                .builder()
                                .isAssembled(false)
                                .build());

                Network.apolloClient.mutate(updateOrderReadyToEatProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderReadyToEatProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderReadyToEatProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        readyToEatProductListener.onResponse(singleItem,"Reassembled Successfully");
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
        return (null != orderReadyToEatProducts ? orderReadyToEatProducts.size() : 0);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.serving)
        TextView serving;
        @BindView(R.id.quantity)
        TextView quantity;
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
        @BindView(R.id.repack)
        LinearLayout repack;
        @BindView(R.id.reassemble)
        LinearLayout reassemble;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

